package dev.yejin1.mushroom_backend.calendar.service;

import dev.yejin1.mushroom_backend.calendar.dto.TagCreateRequestDto;
import dev.yejin1.mushroom_backend.calendar.dto.TagDto;
import dev.yejin1.mushroom_backend.calendar.entity.Schedule;
import dev.yejin1.mushroom_backend.calendar.entity.ScheduleTag;
import dev.yejin1.mushroom_backend.calendar.entity.TagScopeType;
import dev.yejin1.mushroom_backend.calendar.repository.ScheduleTagRepository;
import dev.yejin1.mushroom_backend.org.entity.OrgDept;
import dev.yejin1.mushroom_backend.org.entity.OrgUsr;
import dev.yejin1.mushroom_backend.org.repository.OrgDeptRepository;
import dev.yejin1.mushroom_backend.org.repository.OrgUsrRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagService {

    private final ScheduleTagRepository scheduleTagRepository;
    private final OrgUsrRepository orgUsrRepository;
    private final OrgDeptRepository orgDeptRepository;

    @Transactional
    public TagDto createTag(TagCreateRequestDto dto, Long currentUserId) {
        OrgUsr creator = orgUsrRepository.findById(currentUserId)
                .orElseThrow(() -> new RuntimeException("사용자 없음"));

        if (dto.getPriority() == null) {
            Integer maxPriority = scheduleTagRepository.findMaxPriority().orElse(0);
            dto.setPriority(maxPriority + 1);
        }

        long deptId = creator.getDept().getDeptId();

        ScheduleTag tag = new ScheduleTag();
        tag.setName(dto.getName());
        tag.setColor(dto.getColor());
        tag.setPriority(dto.getPriority());
        tag.setScopeType(dto.getScopeType());

        if (dto.getScopeType() == TagScopeType.PERSONAL) {
            tag.setUsr(creator);
            tag.setDept(null);
        } else if (dto.getScopeType() == TagScopeType.DEPARTMENT) {
            OrgDept dept = orgDeptRepository.findById(deptId)
                    .orElseThrow(() -> new RuntimeException("부서 없음"));
            tag.setDept(dept);
            tag.setUsr(null);
        } else { // COMPANY
            tag.setUsr(null);
            tag.setDept(null);
        }

        ScheduleTag saved = scheduleTagRepository.save(tag);
        return toDto(saved);
    }

    @Transactional(readOnly = true)
    public List<TagDto> getAvailableTags(Long currentUserId, Long currentDeptId) {
        return scheduleTagRepository.findAll().stream()
                .filter(tag ->
                        tag.getScopeType() == TagScopeType.COMPANY ||
                                (tag.getScopeType() == TagScopeType.DEPARTMENT
                                        && tag.getDept() != null
                                        && tag.getDept().getDeptId().equals(currentDeptId)) ||
                                (tag.getScopeType() == TagScopeType.PERSONAL
                                        && tag.getUsr() != null
                                        && tag.getUsr().getUsrId().equals(currentUserId))
                )
                .map(this::toDto)
                .toList();
    }

    private TagDto toDto(ScheduleTag tag) {
        return TagDto.builder()
                .id(tag.getId())
                .name(tag.getName())
                .color(tag.getColor())
                .priority(tag.getPriority())
                .scopeType(tag.getScopeType().name())
                .usrId(tag.getUsr() != null ? tag.getUsr().getUsrId() : null)
                .deptId(tag.getDept() != null ? tag.getDept().getDeptId() : null)
                .build();
    }

    @Transactional
    public void deleteTag(Long id) {
        ScheduleTag tag = scheduleTagRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("태그 정보를 찾을 수 없음"));

        scheduleTagRepository.delete(tag);
    }
}
