package ru.netology.repository;

import org.springframework.stereotype.Repository;
import ru.netology.exception.NotFoundException;
import ru.netology.model.Post;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class PostRepository {
  private final Map<Long, Post> posts = new ConcurrentHashMap<>();
  private final AtomicInteger countOfPosts = new AtomicInteger(0);

  public List<Post> all() {
    return new ArrayList<>(posts.values());
  }

  public Optional<Post> getById(long id) {
    if(posts.containsKey(id)) {
      return Optional.of(posts.get(id));
    }
    return Optional.empty();
  }

  public Post save(Post post) {
    if (post.getId() == 0) {
      post.setId(countOfPosts.incrementAndGet());
    }
    posts.put(post.getId(), post);
    return post;
  }

  public void removeById(long id) {
    if (posts.containsKey(id)) {
      posts.remove(id);
    } else {
      throw new NotFoundException();
    }
  }
}
