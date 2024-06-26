package com.fastcampus.aptner.post.information.dto;

import com.fastcampus.aptner.member.dto.response.PostMemberResponse;
import com.fastcampus.aptner.post.common.enumType.OrderBy;
import com.fastcampus.aptner.post.common.enumType.OrderType;
import com.fastcampus.aptner.post.common.enumType.PostStatus;
import com.fastcampus.aptner.post.common.enumType.SearchType;
import com.fastcampus.aptner.post.information.domain.Information;
import com.fastcampus.aptner.post.information.domain.InformationCategory;
import com.fastcampus.aptner.post.information.domain.InformationType;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public class InformationDTO {

  @Schema(name = "정보공간 생성",description = "정보공간 생성 요청 Body")
    public record InformationPostReqDTO(
            @Schema(description = "정보공간 카테고리 ID")
            Long informationCategoryId,
            @Schema(description = "정보공간 제목")
            String title,
            @Schema(description = "정보공간 내용")
            String contents,
            @Schema(description = "게시글 상태 => PUBLISHED(게시됨), HIDDEN(숨김), DELETED(삭제됨), FORCE_DELETED(강제 삭제됨)")
            PostStatus status){
    }
    @Schema(name = "정보공간 카테고리 생성",description = "정보공간 카테고리 생성 요청 Body")
    public record InformationCategoryReqDTO(
            @Schema(description = "정보공간 카테고리 이름")
            String name,
            @Schema(description = "정보공간 카테고리 타입 => THANKS(인사말),\n" +
                    "     COMPLEX_VIEW(단지전경),\n" +
                    "     TEL_INFO(연락처 정보),\n" +
                    "     FACILITY(커뮤니티 시설),\n" +
                    "     ETC(기타 사이트)")
            InformationType type){
    }

    @Schema(name = "정보공간 카테고리 응답",description = "정보공간 카테고리 응답값")
    public record InformationCategoryRespDTO(
            Long informationCategoryId,
            String name,
            InformationType type){
        public InformationCategoryRespDTO(InformationCategory informationCategory){
            this(informationCategory.getInformationCategoryId(), informationCategory.getName(), informationCategory.getType());
        }
    }

    @Getter
    public static class InformationListRespDTO{
        private final Long informationId;
        private final InformationCategoryRespDTO informationCategory;
        private final PostMemberResponse writer;
        private final String title;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        private final LocalDateTime createdAt;
        private final Long view;
        public InformationListRespDTO(Information information){
            this.informationId = information.getInformationId();
            this.informationCategory = new InformationCategoryRespDTO(information.getInformationCategoryId());
            this.writer = new PostMemberResponse(information.getMemberId());
            this.title = information.getTitle();
            this.createdAt = information.getCreatedAt();
            this.view = information.getView();
        }
    }

    @AllArgsConstructor
    @Getter
    @Setter
    public static class InformationRespDTO {
        private Long informationId;
        private InformationCategoryRespDTO informationCategory;
        private PostMemberResponse writer;
        private String title;
        private String contents;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        private LocalDateTime createdAt;
        private Long view;
        public InformationRespDTO(Information information){
            this.informationId = information.getInformationId();
            this.informationCategory = new InformationCategoryRespDTO(information.getInformationCategoryId());
            this.writer = new PostMemberResponse(information.getMemberId());
            this.title = information.getTitle();
            this.contents = information.getContents();
            this.createdAt = information.getCreatedAt();
            this.view = information.getView();
        }
    }
    @Builder
    @Getter
    @Setter
    public static class InformationSearchReqDTO{
        private int pageNumber;
        private int pageSize;
        private Pageable pageable;
        private Long apartmentId;
        private SearchType searchType;
        private OrderType orderType;
        private OrderBy orderBy;
        private String keyword;
        private InformationType informationType;
        private Long categoryId;
    }

}
