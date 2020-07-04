package io.github.portlek.configs.configuration.comments;

public interface Commentable {

    void setComment(String path, String comment, CommentType type);

    default void setComment(final String path, final String comment) {
        this.setComment(path, comment, CommentType.BLOCK);
    }

    String getComment(String path, CommentType type);

    default String getComment(final String path) {
        return this.getComment(path, CommentType.BLOCK);
    }

}
