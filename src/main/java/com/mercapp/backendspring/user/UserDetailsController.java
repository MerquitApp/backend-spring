package com.mercapp.backendspring.user;

import com.mercapp.backendspring.user.dtos.UpdateUserDTO;
import com.mercapp.backendspring.user.models.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserDetailsController {
    @Autowired
    private UserDetailsService userDetailsService;

    @GetMapping()
    public ResponseEntity<UserDetails> get() {
        UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(user);
    }

    @PatchMapping()
    public ResponseEntity<UserDetails> update(@RequestBody UpdateUserDTO updateUserDTO) {
        UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails updatedUser = this.userDetailsService.update(user.getId(), updateUserDTO);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping()
    public ResponseEntity<UserDetails> deleteById() {
        UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        this.userDetailsService.delete(user.getId());
        return ResponseEntity.ok(user);
    }
}
