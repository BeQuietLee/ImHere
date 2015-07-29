package com.leili.imhere.entity;

/**
 * Created by Lei.Li on 7/27/15 10:42 AM.
 */
public interface ILikePosition {
    public void likePosition(Position position);
    public void loadMore(int offset, int pageSize);
}
