package com.codeterian.common.infrastructure.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.hibernate.annotations.Comment;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {

    @CreatedDate
    @Column(updatable = false, columnDefinition = "TIMESTAMP", nullable = false)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Comment("생성일시")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(columnDefinition = "TIMESTAMP")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Comment("수정일시")
    private LocalDateTime updatedAt;

    @Column(columnDefinition = "TIMESTAMP")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Comment("삭제일시")
    private LocalDateTime deletedAt;


   @Column(updatable = false)
   private Long createdBy;

   private Long updatedBy;

    private Long deletedBy;

    private boolean isDeleted = false;

    public void delete(Long handlerId) {
        this.deletedBy = handlerId;
        this.isDeleted = true;
        this.deletedAt = LocalDateTime.now();
    }

    public void undoDelete(Integer handlerId) {
        this.deletedBy = null;
        this.deletedAt = null;
        this.isDeleted = false;
    }

    public void createBy(Long handlerId) {
        this.createdBy = handlerId;
    }

    public void updateBy(Long handlerId) {
        this.updatedBy = handlerId;
    }

}
