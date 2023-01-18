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
 * @TableName file_folder
 */
@AllArgsConstructor
@Data
@Builder
@TableName(value ="file_folder")
public class FileFolder implements Serializable {
    /**
     * 文件夹ID
     */
    @TableId(type = IdType.AUTO)
    private Integer fileFolderId;

    /**
     * 文件夹名称
     */
    private String fileFolderName;

    /**
     * 父文件夹ID
     */
    private Integer parentFolderId;

    /**
     * 所属文件仓库ID
     */
    private Integer fileStoreId;

    /**
     * 创建时间
     */
    private Date time;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * 文件夹ID
     */
    public Integer getFileFolderId() {
        return fileFolderId;
    }

    /**
     * 文件夹ID
     */
    public void setFileFolderId(Integer fileFolderId) {
        this.fileFolderId = fileFolderId;
    }

    /**
     * 文件夹名称
     */
    public String getFileFolderName() {
        return fileFolderName;
    }

    /**
     * 文件夹名称
     */
    public void setFileFolderName(String fileFolderName) {
        this.fileFolderName = fileFolderName;
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
     * 所属文件仓库ID
     */
    public Integer getFileStoreId() {
        return fileStoreId;
    }

    /**
     * 所属文件仓库ID
     */
    public void setFileStoreId(Integer fileStoreId) {
        this.fileStoreId = fileStoreId;
    }

    /**
     * 创建时间
     */
    public Date getTime() {
        return time;
    }

    /**
     * 创建时间
     */
    public void setTime(Date time) {
        this.time = time;
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
        FileFolder other = (FileFolder) that;
        return (this.getFileFolderId() == null ? other.getFileFolderId() == null : this.getFileFolderId().equals(other.getFileFolderId()))
            && (this.getFileFolderName() == null ? other.getFileFolderName() == null : this.getFileFolderName().equals(other.getFileFolderName()))
            && (this.getParentFolderId() == null ? other.getParentFolderId() == null : this.getParentFolderId().equals(other.getParentFolderId()))
            && (this.getFileStoreId() == null ? other.getFileStoreId() == null : this.getFileStoreId().equals(other.getFileStoreId()))
            && (this.getTime() == null ? other.getTime() == null : this.getTime().equals(other.getTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getFileFolderId() == null) ? 0 : getFileFolderId().hashCode());
        result = prime * result + ((getFileFolderName() == null) ? 0 : getFileFolderName().hashCode());
        result = prime * result + ((getParentFolderId() == null) ? 0 : getParentFolderId().hashCode());
        result = prime * result + ((getFileStoreId() == null) ? 0 : getFileStoreId().hashCode());
        result = prime * result + ((getTime() == null) ? 0 : getTime().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", fileFolderId=").append(fileFolderId);
        sb.append(", fileFolderName=").append(fileFolderName);
        sb.append(", parentFolderId=").append(parentFolderId);
        sb.append(", fileStoreId=").append(fileStoreId);
        sb.append(", time=").append(time);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}