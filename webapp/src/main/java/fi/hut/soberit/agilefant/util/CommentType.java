package fi.hut.soberit.agilefant.util;

public enum CommentType {
    STORY_COMMENT{
        @Override
        public String toString() {
            return "story";
        }
    },
    TASK_COMMENT{
        @Override
        public String toString() {
            return "task";
        }
    }
}
