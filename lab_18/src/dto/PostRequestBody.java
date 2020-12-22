package dto;

import models.Post;
import models.Tag;

public class PostRequestBody {
  public Post post;
  public Tag[] tags;
}
