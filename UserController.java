package lk.ac.vau.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import lk.ac.vau.Model.Comment;
import lk.ac.vau.Model.Post;
import lk.ac.vau.Model.User;
import lk.ac.vau.Repo.UserRepo;

@RestController
@RequestMapping("/user")
public class UserController {
	@Autowired
	UserRepo context;

	@GetMapping
	public List<User> getAll() {
		List<User> users=context.findAll();
		for (User u: users)
		{
			String ownUrl= linkTo(UserController.class).slash(u.getUserdId()).toString();
			String postUrl= linkTo(UserController.class).slash(u.getUserdId()).slash("post").toString();
			String commentUrl= linkTo(UserController.class).slash(u.getUserdId()).slash("comment").toString();
			
			u.addLink(ownUrl, "own");
			u.addLink(postUrl, "post");
			u.addLink(commentUrl, "comment");
		}
		return users;
	}

	//Postman : http://localhost:8080/user/uid100
	@GetMapping("/{id}")
	public User get(@PathVariable("id") String id) {
		
		//http://localhost:8080/user/uid101/post
		String postUrl= linkTo(UserController.class).slash(id).slash("post").toString();
		
		//http://localhost:8080/user/uid101/comment
		String commentUrl= linkTo(UserController.class).slash(id).slash("comment").toString();
		
		User user =context.findById(id).get();
		user.addLink(postUrl, "Post");
		user.addLink(commentUrl, "Comment");
		return user;
	}

	@PostMapping
	public void add(@RequestBody User user) {
		context.save(user);
	}

	@PutMapping
	public void update(@RequestBody User user) {
		context.save(user);
	}

	@DeleteMapping("/{id}")
	public void delete(@PathVariable("id") String id) {
		context.deleteById(id);
	}
	
	@GetMapping("/{id}/post")
	public List<Post> getPost(@PathVariable("id") String id) {
		return context.findById(id).get().getPosts();
	}
	
	@GetMapping("/{id}/comment")
	public List<Comment> getComment(@PathVariable("id") String id) {
		return context.findById(id).get().getComments();
	}
	
	//create a post by user
	@PostMapping("/{uid}/post")
	public void addpost(@PathVariable("uid") String id,@RequestBody List<Post> posts) {
		User user =context.findById(id).get();
		user.setPosts(posts);
		//post.setOwner(context.findById(id).get());
		context.save(user);
	}  
	//http://localhost:8080/user/uid100/post

}
