/**
 * OrgUsrRepository
 * <p>
 * 사용자 정보 테이블 Repository
 *
 * <p>
 * </p>
 *
 * @author Yejin1
 * @since 2025-05-17
 */
package dev.yejin1.mushroom_backend.org.repository;

import dev.yejin1.mushroom_backend.org.dto.OrgModalDto;
import dev.yejin1.mushroom_backend.org.dto.OrgUserSimpleDto;
import dev.yejin1.mushroom_backend.org.entity.OrgUsr;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface OrgUsrRepository extends JpaRepository<OrgUsr, Long> {

    Optional<OrgUsr> findByLoginId(String loginId);

    Optional<OrgUsr> findByUsrNmAndEmpNo(String usrNm, String empNo);

    @Query("""
                SELECT new dev.yejin1.mushroom_backend.org.dto.OrgUserSimpleDto(
                    u.usrId, u.usrNm, u.empNo, p.posNm, d.deptNm, u.email
                )
                FROM OrgUsr u
                LEFT JOIN u.pos p
                LEFT JOIN u.dept d
            """)
    List<OrgUserSimpleDto> findAllSimpleUsers();


    @Query("""
            SELECT new dev.yejin1.mushroom_backend.org.dto.OrgModalDto(
                u.usrId,
                u.usrNm,
                u.empNo,
                p.posNm,
                d.deptNm,
                u.extensionNo,
                u.email,
                u.jobDesc,
                u.profileBio
            )
            FROM OrgUsr u
            LEFT JOIN u.pos p
            LEFT JOIN u.dept d
            WHERE u.status = 'A'
            ORDER BY d.deptNm, p.posId
            """)
    List<OrgModalDto> findAllForOrgView();


}
