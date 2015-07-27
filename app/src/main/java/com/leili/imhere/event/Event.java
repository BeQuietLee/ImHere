package com.leili.imhere.event;

import com.leili.imhere.entity.Position;

/**
 * Created by Lei.Li on 7/25/15 3:15 PM.
 */
public class Event {
    /**
     * 定位地点
     */
    public static class LocatePositionEvent {
        public LocatePositionEvent (Position position) {
            this.position = position;
        }
        private Position position;

        public Position getPosition() {
            return position;
        }

        public void setPosition(Position position) {
            this.position = position;
        }
    }

    /**
     * 收藏变动
     */
    public static class LikeEvent {
    }
}
