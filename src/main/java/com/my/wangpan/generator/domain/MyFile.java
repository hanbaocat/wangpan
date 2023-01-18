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
 * @TableName my_file
 */
@TableName(value ="my_file")
@AllArgsConstructor
@Data
@Builder
public class MyFile implements Serializable {
    /**
     * 文件ID
     */
    @TableId(type = IdType.AUTO)
    private Integer myFileId;

    /**
     * 文件名
     */
    private String myFileName;

    /**
     * 文件仓库ID
     */
    private Integer fileStoreId;

    /**
     * 文件存储路径
     */
    private String myFilePath;

    /**
     * 下载次数
     */
    private Integer downloadTime;

    /**
     * 上传时间
     */
    private Date uploadTime;

    /**
     * 父文件夹ID
     */
    private Integer parentFolderId;

    /**
     * 文件大小
     */
    private Integer size;

    /**
     * 文件类型
     */
    private Integer type;

    /**
     * 文件后缀
     */
    private String postfix;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * 文件ID
     */
    public Integer getMyFileId() {
        return myFileId;
    }

    /**
     * 文件ID
     */
    public void setMyFileId(Integer myFileId) {
        this.myFileId = myFileId;
    }

    /**
     * 文件名
     */
    public String getMyFileName() {
        return myFileName;
    }

    /**
     * 文件名
     */
    public void setMyFileName(String myFileName) {
        this.myFileName = myFileName;
    }

    /**
     * 文件仓库ID
     */
    public Integer getFileStoreId() {
        return fileStoreId;
    }

    /**
     * 文件仓库ID
     */
    public void setFileStoreId(Integer fileStoreId) {
        this.fileStoreId = fileStoreId;
    }

    /**
     * 文件存储路径
     */
    public String getMyFilePath() {
        return myFilePath;
    }

    /**
     * 文件存储路径
     */
    public void setMyFilePath(String myFilePath) {
        this.myFilePath = myFilePath;
    }

    /**
     * 下载次数
     */
    public Integer getDownloadTime() {
        return downloadTime;
    }

    /**
     * 下载次数
     */
    public void setDownloadTime(Integer downloadTime) {
        this.downloadTime = downloadTime;
    }

    /**
     * 上传时间
     */
    public Date getUploadTime() {
        return uploadTime;
    }

    /**
     * 上传时间
     */
    public void setUploadTime(Date uploadTime) {
        this.uploadTime = uploadTime;
    }

    /**
     * 父文件夹ID
     */
    public Integer getParentFolderId() {
        return parentFolderId;
    }

    /**
     * 父文件夹ID
     */
    public void setParentFolderId(Integer parentFolderId) {
        this.parentFolderId = parentFolderId;
    }

    /**
     * 文件大小
     */
    public Integer getSize() {
        return size;
    }

    /**
     * 文件大小
     */
    public void setSize(Integer size) {
        this.size = size;
    }

    /**
     * 文件类型
     */
    public Integer getType() {
        return type;
    }

    /**
     * 文件类型
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * 文件后缀
     */
    public String getPostfix() {
        return postfix;
    }

    /**
     * 文件后缀
     */
    public void setPostfix(String postfix) {
        this.postfix = postfix;
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
        MyFile other = (MyFile) that;
        return (this.getMyFileId() == null ? other.getMyFileId() == null : this.getMyFileId().equals(other.getMyFileId()))
            && (this.getMyFileName() == null ? other.getMyFileName() == null : this.getMyFileName().equals(other.getMyFileName()))
            && (this.getFileStoreId() == null ? other.getFileStoreId() == null : this.getFileStoreId().equals(other.getFileStoreId()))
            && (this.getMyFilePath() == null ? other.getMyFilePath() == null : this.getMyFilePath().equals(other.getMyFilePath()))
            && (this.getDownloadTime() == null ? other.getDownloadTime() == null : this.getDownloadTime().equals(other.getDownloadTime()))
            && (this.getUploadTime() == null ? other.getUploadTime() == null : this.getUploadTime().equals(other.getUploadTime()))
            && (this.getParentFolderId() == null ? other.getParentFolderId() == null : this.getParentFolderId().equals(other.getParentFolderId()))
            && (this.getSize() == null ? other.getSize() == null : this.getSize().equals(other.getSize()))
            && (this.getType() == null ? other.getType() == null : this.getType().equals(other.getType()))
            && (this.getPostfix() == null ? other.getPostfix() == null : this.getPostfix().equals(other.getPostfix()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getMyFileId() == null) ? 0 : getMyFileId().hashCode());
        result = prime * result + ((getMyFileName() == null) ? 0 : getMyFileName().hashCode());
        result = prime * result + ((getFileStoreId() == null) ? 0 : getFileStoreId().hashCode());
        result = prime * result + ((getMyFilePath() == null) ? 0 : getMyFilePath().hashCode());
        result = prime * result + ((getDownloadTime() == null) ? 0 : getDownloadTime().hashCode());
        result = prime * result + ((getUploadTime() == null) ? 0 : getUploadTime().hashCode());
        result = prime * result + ((getParentFolderId() == null) ? 0 : getParentFolderId().hashCode());
        result = prime * result + ((getSize() == null) ? 0 : getSize().hashCode());
        result = prime * result + ((getType() == null) ? 0 : getType().hashCode());
        result = prime * result + ((getPostfix() == null) ? 0 : getPostfix().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", myFileId=").append(myFileId);
        sb.append(", myFileName=").append(myFileName);
        sb.append(", fileStoreId=").append(fileStoreId);
        sb.append(", myFilePath=").append(myFilePath);
        sb.append(", downloadTime=").append(downloadTime);
        sb.append(", uploadTime=").append(uploadTime);
        sb.append(", parentFolderId=").append(parentFolderId);
        sb.append(", size=").append(size);
        sb.append(", type=").append(type);
        sb.append(", postfix=").append(postfix);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}