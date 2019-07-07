package models.comment;

import models.product.Product;

public class CommentManager {
    public static void attachCommentOnProduct(Product product, Comment comment) {
        ProductComment productComment = ProductComment.find.byId(product.id);
        if(productComment != null) {
            productComment.comments.add(comment);
            productComment.update();
        } else {
            productComment = new ProductComment(product, comment);
            productComment.save();
        }
        comment.productcomment = productComment;
        comment.update();
    }
}
