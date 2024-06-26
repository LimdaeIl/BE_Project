package com.fastcampus.aptner.post.communication.service.admin;

import com.fastcampus.aptner.global.error.RestAPIException;
import com.fastcampus.aptner.jwt.util.JWTMemberInfoDTO;
import com.fastcampus.aptner.member.domain.RoleName;
import com.fastcampus.aptner.post.common.error.PostErrorCode;
import com.fastcampus.aptner.post.communication.domain.Communication;
import com.fastcampus.aptner.post.communication.repository.CommunicationRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

import static com.fastcampus.aptner.post.common.error.PostErrorCode.NO_SUCH_POST;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommunicationAdminServiceImpl implements CommunicationAdminService{

    CommunicationRepository communicationRepository;

    @Override
    @Transactional
    public ResponseEntity<HttpStatus> deleteCommunicationAdmin(JWTMemberInfoDTO userToken, Long communicationId) {
        Communication communication = communicationRepository.findById(communicationId).orElseThrow(()->new RestAPIException(NO_SUCH_POST));
        checkApartmentByCommunication(userToken, communication);
        /*if (userToken.getRoleName().equals(RoleName.ADMIN.getRoleName())) {
            communication.deleteCommunicationAdmin();
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }*/
        //강제삭제 버튼(나중에 삭제할 것)
        communication.deleteCommunication();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void checkApartmentByCommunication(JWTMemberInfoDTO userToken,Communication communication){
        if (!Objects.equals(userToken.getApartmentId(), communication.getCommunicationCategoryId().getApartmentId().getApartmentId())){
            throw new RestAPIException(PostErrorCode.NOT_ALLOWED_APARTMENT);
        }
    }
}
