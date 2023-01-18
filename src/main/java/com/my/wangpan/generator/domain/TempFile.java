package com.my.wangpan.generator.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @TableName temp_file
 */
@TableName(value ="temp_file")
@AllArgsConstructor
@Data
@Builder
public class TempFile implements Serializable {
    /**
     * 临时文件ID
     */
    @TableId(type = IdType.AUTO)
    private Integer fileId;

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 文件大小
     */
    private String size;

    /**
     * 上传时间：4小时后删除
     */
    private Date uploadTime;

    /**
     * 文件在FTP上的存放路径
     */
    private String filePath;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * 临时文件ID
     */
    public Integer getFileId() {
        return fileId;
    }

    /**
     * 临时文件ID
     */
    public void setFileId(Integer fileId) {
        this.fileId = fileId;
    }

    /**
     * 文件名
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * 文件名
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * 文件大小
     */
    public String getSize() {
        return size;
    }

    /**
     * 文件大小
     */
    public void setSize(String size) {
        this.size = size;
    }

    /**
     * 上传时间：4小时后删除
     */
    public Date getUploadTime() {
        return uploadTime;
    }

    /**
     * 上传时间：4小时后删除
     */
    public void setUploadTime(Date uploadTime) {
        this.uploadTime = uploadTime;
    }

    /**
     * 文件在FTP上的存放路径
     */
    public String getFilePath() {
        return filePath;
    }

    /**
     * 文件在FTP上的存放路径
     */
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        TempFile other = (TempFile) that;
        return (this.getFileId() == null ? other.getFileId() == null : this.getFileId().equals(other.getFileId()))
            && (this.getFileName() == null ? other.getFileName() == null : this.getFileName().equals(other.getFileName()))
            && (this.getSize() == null ? other.getSize() == null : this.getSize().equals(other.getSize()))
            && (this.getUploadTime() == null ? other.getUploadTime() == null : this.getUploadTime().equals(other.getUploadTime()))
            && (this.getFilePath() == null ? other.getFilePath() == null : this.getFilePath().equals(other.getFilePath()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getFileId() == null) ? 0 : getFileId().hashCode());
        result = prime * result + ((getFileName() == null) ? 0 : getFileName().hashCode());
        result = prime * result + ((getSize() == null) ? 0 : getSize().hashCode());
        result = prime * result + ((getUploadTime() == null) ? 0 : getUploadTime().hashCode());
        result = prime * result + ((getFilePath() == null) ? 0 : getFilePath().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", fileId=").append(fileId);
        sb.append(", fileName=").append(fileName);
        sb.append(", size=").append(size);
        sb.append(", uploadTime=").append(uploadTime);
        sb.append(", filePath=").append(filePath);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}