package com.my.wangpan.generator.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * 
 * @TableName file_store
 */
@TableName(value ="file_store")
@AllArgsConstructor
@Data
@Builder
public class FileStore implements Serializable {
    /**
     * 文件仓库ID
     */
    @TableId(type = IdType.AUTO)
    private Integer fileStoreId;

    /**
     * 主人ID
     */
    private Integer userId;

    /**
     * 当前容量（单位KB）
     */
    private Integer currentSize;

    /**
     * 最大容量（单位KB）
     */
    private Integer maxSize;

    /**
     * 仓库权限，0可上传下载、1不允许上传可以下载、2不可以上传下载
     */
    private Integer permission;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

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
     * 主人ID
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * 主人ID
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * 当前容量（单位KB）
     */
    public Integer getCurrentSize() {
        return currentSize;
    }

    /**
     * 当前容量（单位KB）
     */
    public void setCurrentSize(Integer currentSize) {
        this.currentSize = currentSize;
    }

    /**
     * 最大容量（单位KB）
     */
    public Integer getMaxSize() {
        return maxSize;
    }

    /**
     * 最大容量（单位KB）
     */
    public void setMaxSize(Integer maxSize) {
        this.maxSize = maxSize;
    }

    /**
     * 仓库权限，0可上传下载、1不允许上传可以下载、2不可以上传下载
     */
    public Integer getPermission() {
        return permission;
    }

    /**
     * 仓库权限，0可上传下载、1不允许上传可以下载、2不可以上传下载
     */
    public void setPermission(Integer permission) {
        this.permission = permission;
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
        FileStore other = (FileStore) that;
        return (this.getFileStoreId() == null ? other.getFileStoreId() == null : this.getFileStoreId().equals(other.getFileStoreId()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getCurrentSize() == null ? other.getCurrentSize() == null : this.getCurrentSize().equals(other.getCurrentSize()))
            && (this.getMaxSize() == null ? other.getMaxSize() == null : this.getMaxSize().equals(other.getMaxSize()))
            && (this.getPermission() == null ? other.getPermission() == null : this.getPermission().equals(other.getPermission()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getFileStoreId() == null) ? 0 : getFileStoreId().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getCurrentSize() == null) ? 0 : getCurrentSize().hashCode());
        result = prime * result + ((getMaxSize() == null) ? 0 : getMaxSize().hashCode());
        result = prime * result + ((getPermission() == null) ? 0 : getPermission().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", fileStoreId=").append(fileStoreId);
        sb.append(", userId=").append(userId);
        sb.append(", currentSize=").append(currentSize);
        sb.append(", maxSize=").append(maxSize);
        sb.append(", permission=").append(permission);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}