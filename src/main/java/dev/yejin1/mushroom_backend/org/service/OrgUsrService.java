package dev.yejin1.mushroom_backend.org.service;

import dev.yejin1.mushroom_backend.org.dto.OrgModalDto;
import dev.yejin1.mushroom_backend.org.dto.OrgUserSimpleDto;
import dev.yejin1.mushroom_backend.org.repository.OrgUsrRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrgUsrService {

    private final OrgUsrRepository orgUsrRepository;

    public List<OrgUserSimpleDto> getAllUsers() {
        return orgUsrRepository.findAllSimpleUsers();
    }

    public List<OrgModalDto> getOrgModalList() {
        return orgUsrRepository.findAllForOrgView();
    }

}
