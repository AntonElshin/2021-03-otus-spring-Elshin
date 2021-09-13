package ru.otus.homework.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "REF_FILEUPLOADHISTORY")
public class FileUploadHistoryEntity {

    @Id
    @Column(name = "HISTORYID")
    @SequenceGenerator( name = "REF_FILEUPLOADHISTORY_SEQ", sequenceName = "REF_FILEUPLOADHISTORY_SEQ", allocationSize = 1, initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "REF_FILEUPLOADHISTORY_SEQ")
    private Long id;

    @Column(name = "UPLOADDATESTART")
    private LocalDateTime uploadDateStart;

    @Column(name = "UPLOADDATEEND")
    private LocalDateTime uploadDateEnd;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "USERINFO")
    private String userInfo;

    @Column(name = "VERSIONNUMBER")
    private Integer versionNumber;

    @OneToOne
    @JoinColumn(name = "FILETYPEID")
    private ReferenceItemEntity fileType;

    @Column(name = "FILENAME")
    private String fileName;

    @Column(name = "FILECONTENT")
    private String fileContent;

    @Column(name = "ERRORMESSAGE")
    private String errorMessage;

}
