package dev.yejin1.mushroom_backend.org.service;

import dev.yejin1.mushroom_backend.org.dto.SubscriptCheckRequest;
import dev.yejin1.mushroom_backend.org.dto.SubscriptCheckResponse;
import dev.yejin1.mushroom_backend.org.entity.OrgUsr;
import dev.yejin1.mushroom_backend.org.repository.OrgUsrRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JoinService {

    private final OrgUsrRepository orgUsrRepository;

    public SubscriptCheckResponse checkSubscript(SubscriptCheckRequest request) {
        String usrNm = request.getUsrNm();
        String empNo= request.getEmpNo();

        Optional<OrgUsr> chkUsr = orgUsrRepository.findByUsrNmAndEmpNo(usrNm, empNo);
        SubscriptCheckResponse response = new SubscriptCheckResponse();

        if(!chkUsr.isPresent()){
            response.setResult(0);
            response.setMessage("일치하는 직원 정보가 없습니다.");
        }
        else if (chkUsr.get().getLoginId() == null || chkUsr.get().getLoginId().equals("")){
            response.setResult(1);
            response.setMessage("확인되었습니다.");
        }
        else {
            response.setResult(2);
            response.setMessage("이미 가입된 정보입니다. \n 아이디를 분실하신 경우 관리자에게 문의하시기 바랍니다.");
        }


        return response;
    }

    public Boolean checkId(String loginId){
        Optional<OrgUsr> user = orgUsrRepository.findByLoginId(loginId);

        if(user.isPresent()) return false;
        return true;
    }

}
