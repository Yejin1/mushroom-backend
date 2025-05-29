/**
 * OrgUsrRepository
 *
 * 사용자 정보 테이블 Repository
 *
 * <p>
 * </p>
 *
 * @author Yejin1
 * @since 2025-05-17
 */
package dev.yejin1.mushroom_backend.org.repository;

import dev.yejin1.mushroom_backend.org.entity.OrgUsr;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface OrgUsrRepository  extends JpaRepository<OrgUsr, Long> {

    Optional<OrgUsr> findByLoginId(String loginId);

    Optional<OrgUsr> findByUsrNmAndEmpNo(String usrNm, String empNo);

}
