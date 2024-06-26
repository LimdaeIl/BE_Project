package com.fastcampus.aptner.post.announcement.service.admin;

import com.fastcampus.aptner.apartment.domain.Apartment;
import com.fastcampus.aptner.apartment.service.ApartmentCommonService;
import com.fastcampus.aptner.global.error.RestAPIException;
import com.fastcampus.aptner.jwt.util.JWTMemberInfoDTO;
import com.fastcampus.aptner.member.domain.Member;
import com.fastcampus.aptner.member.service.MemberCommonService;
import com.fastcampus.aptner.post.announcement.domain.Announcement;
import com.fastcampus.aptner.post.announcement.domain.AnnouncementCategory;
import com.fastcampus.aptner.post.announcement.dto.AnnouncementDTO;
import com.fastcampus.aptner.post.announcement.repository.AnnouncementRepository;
import com.fastcampus.aptner.post.announcement.service.AnnouncementCommonService;
import com.fastcampus.aptner.post.common.error.PostErrorCode;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Objects;

import static com.fastcampus.aptner.post.common.error.PostErrorCode.NO_SUCH_POST;

@Service
@Slf4j
@RequiredArgsConstructor
public class AnnouncementAdminServiceImpl implements AnnouncementAdminService {

    private final AnnouncementRepository announcementRepository;

    private final MemberCommonService memberCommonService;
    private final ApartmentCommonService apartmentCommonService;
    private final AnnouncementCommonService announcementCommonService;

    /**
     * 공지사항 업로드
     *
     * @param userToken   토큰 정보
     * @param apartmentId 현재 아파트단지 ID
     * @param dto         공지사항 정보 (제목, 내용, 중요도, 카테고리, 상태)
     */
    @Override
    public ResponseEntity<HttpStatus> uploadAnnouncement(JWTMemberInfoDTO userToken, Long apartmentId, AnnouncementDTO.AnnouncementPostReqDTO dto) {

        UserAndAPT userAndAPT = getUserAndAPT(userToken, apartmentId);
        AnnouncementCategory announcementCategory = announcementCommonService.getAnnouncementCategory(dto.announcementCategoryId());
        Announcement announcement = Announcement.from(userAndAPT.member, announcementCategory, dto);
        //Todo 예외처리
        announcementRepository.save(announcement);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * 공지사항 수정
     *
     * @param userToken      토큰 정보
     * @param announcementId 수정하려는 게시글
     * @param dto            수정 내용 (제목, 내용, 중요도, 공지 타입, 상태)
     */
    @Override
    @Transactional
    public ResponseEntity<HttpStatus> updateAnnouncement(JWTMemberInfoDTO userToken, Long announcementId, AnnouncementDTO.AnnouncementPostReqDTO dto) {
        Announcement announcement = getAnnouncement(announcementId);
        checkApartmentByAnnouncement(userToken, announcement);
        AnnouncementCategory announcementCategory = announcementCommonService.getAnnouncementCategory(dto.announcementCategoryId());
        //Todo 권한 확인
        //Todo 예외처리
        announcement.updateAnnouncement(announcementCategory, dto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 공지사항 삭제
     *
     * @param userToken      토큰 정보
     * @param announcementId 삭제하려는 게시글
     */
    @Override
    @Transactional
    public ResponseEntity<HttpStatus> deleteAnnouncement(JWTMemberInfoDTO userToken, Long announcementId) {
        Announcement announcement = getAnnouncement(announcementId);
        checkApartmentByAnnouncement(userToken, announcement);
        //Todo 권한 확인
        //Todo 예외처리
        announcement.deleteAnnouncement();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 공지사항 숨기기
     *
     * @param userToken      토큰 정보
     * @param announcementId 숨기려는 게시글
     */
    @Override
    @Transactional
    public ResponseEntity<HttpStatus> hideAnnouncement(JWTMemberInfoDTO userToken, Long announcementId) {
        Announcement announcement = getAnnouncement(announcementId);
        checkApartmentByAnnouncement(userToken, announcement);
        //Todo 권한 확인
        //Todo 예외처리
        announcement.hideAnnouncement();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @AllArgsConstructor
    public static class UserAndAPT {
        Member member;
        Apartment apartment;
    }

    private UserAndAPT getUserAndAPT(JWTMemberInfoDTO userToken, Long apartmentId) {
        if (!Objects.equals(userToken.getApartmentId(), apartmentId)) {
            throw new RestAPIException(PostErrorCode.NOT_ALLOWED_APARTMENT);
        }
        Member member = memberCommonService.getUserByToken(userToken);
        Apartment apartment = apartmentCommonService.getApartmentById(apartmentId);
        return new UserAndAPT(member, apartment);
    }

    private void checkApartmentByAnnouncement(JWTMemberInfoDTO userToken, Announcement announcement) {
        if (!Objects.equals(userToken.getApartmentId(), announcement.getAnnouncementCategoryId().getApartmentId().getApartmentId())) {
            throw new RestAPIException(PostErrorCode.NOT_ALLOWED_APARTMENT);
        }
    }

    private Announcement getAnnouncement(Long announcementId){
        return  announcementRepository.findById(announcementId).orElseThrow(()->new RestAPIException(NO_SUCH_POST));
    }
}
