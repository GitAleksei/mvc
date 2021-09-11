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
import java.util.stream.Collectors;

@Repository
public class PostRepository {
  private final Map<Long, Post> posts = new ConcurrentHashMap<>();
  private final AtomicInteger countOfPosts = new AtomicInteger(0);

  public List<Post> all() {
    return posts.values().stream()
            .filter(post -> !post.isRemoved())
            .collect(Collectors.toList());
  }

  public Optional<Post> getById(long id) {
    if(posts.containsKey(id) && !(posts.get(id).isRemoved())) {
      return Optional.of(posts.get(id));
    }
    return Optional.empty();
  }

  public Post save(Post post) {
    if (post.getId() == 0) {
      post.setId(countOfPosts.incrementAndGet());
    } else if (posts.get(post.getId()).isRemoved()) {
      throw new NotFoundException();
    }
    posts.put(post.getId(), post);
    return post;
  }

  public void removeById(long id) {
    if (posts.containsKey(id) && !posts.get(id).isRemoved()) {
      posts.get(id).setRemoved(true);
    } else {
      throw new NotFoundException();
    }
  }
}
