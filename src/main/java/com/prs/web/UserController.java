package com.prs.web;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import com.prs.business.JsonResponse;
import com.prs.business.User;
import com.prs.db.UserRepository;

@CrossOrigin
@RestController
@RequestMapping("/users")
public class UserController {
	@Autowired
	private UserRepository userRepository;
	
	private static final String encryptionKey           = "ABCDEFGHIJKLMNOP";
    private static final String characterEncoding       = "UTF-8";
    private static final String cipherTransformation    = "AES/CBC/PKCS5PADDING";
    private static final String aesEncryptionAlgorithem = "AES";
	
	@GetMapping("/")
	public JsonResponse getAll() {
		JsonResponse jr = null;
		try {
			jr = JsonResponse.getInstance(userRepository.findAll());
		} catch (Exception e) {
			jr = JsonResponse.getInstance(e);
		}
		return jr;
	}

	@GetMapping("/{id}")
	public JsonResponse get(@PathVariable int id) {
		JsonResponse jr = null;
		try {
			User p = userRepository.findById(id).get();
			if (p != null) {
				//System.out.println(p.getPassword());
				p.setPassword(decrypt(p.getPassword()));
				//System.out.println(p.getPassword());
				//System.out.println(p.toString());
				jr = JsonResponse.getInstance(p);
			} else {
				jr = JsonResponse.getInstance("No user found for id: " + id);
			}
		} catch (Exception e) {
			jr = JsonResponse.getInstance(e);
		}
		return jr;

	}

	@PostMapping("/")
	public JsonResponse add(@RequestBody User user) {
		JsonResponse jr = null;
		try {
			user.setPassword(encrypt(user.getPassword()));
			jr = JsonResponse.getInstance(userRepository.save(user));
		} catch (Exception e) {
			jr = JsonResponse.getInstance(e);
		}
		return jr;
	}

	@DeleteMapping("/")
	public JsonResponse delete(@RequestBody User user) {
		JsonResponse jr = null;
		try {
			if (userRepository.existsById(user.getId())) {
				userRepository.delete(user);
				jr = JsonResponse.getInstance("User deleted");
			} else {
				jr = JsonResponse.getInstance("No User: " + user);
			}
		} catch (Exception e) {
			jr = JsonResponse.getInstance(e);
		}
		return jr;
	}

	@DeleteMapping("/{id}")
	public JsonResponse delete(@PathVariable int id) {
		JsonResponse jr = null;
		try {
			if (userRepository.existsById(id)) {
				jr = JsonResponse.getInstance(userRepository.findById(id));
				userRepository.deleteById(id);
			} else {
				jr = JsonResponse.getInstance("No User with id: " + id);
			}
		} catch (Exception e) {
			jr = JsonResponse.getInstance(e);
		}
		return jr;
	}

	@PutMapping("/")
	public JsonResponse update(@RequestBody User user) {
		JsonResponse jr = null;
		try {
			if (userRepository.existsById(user.getId())) {
				user.setPassword(encrypt(user.getPassword()));
				jr = JsonResponse.getInstance(userRepository.save(user));
			} else {
				jr = JsonResponse.getInstance("No User exists with id: " + user.getId());
			}
		} catch (Exception e) {
			jr = JsonResponse.getInstance(e);
		}
		return jr;
	}

	@PostMapping("/authenticate")
	public JsonResponse login(@RequestBody User user) {
		JsonResponse jr = null;
		String message = "";
		try {
			user.setPassword(encrypt(user.getPassword()));
			Optional<User> p = userRepository.findByUserNameAndPassword(user.getUserName(), user.getPassword());
			if (p.isPresent()) {
				jr = JsonResponse
						.getInstance(userRepository.findByUserNameAndPassword(user.getUserName(), user.getPassword()));
			} else {
				Optional<User> u = userRepository.findByUserName(user.getUserName());
				// Optional<User> pas = userRepository.findByPassword(user.getPassword());
				if (!u.isPresent()) {
					message += "No account exists with username " + user.getUserName();
				} else {
					message += "The password does not match the username " + user.getUserName();
				}
				jr = JsonResponse.getInstance(message);
			}
		} catch (Exception e) {
			jr = JsonResponse.getInstance(e);
		}
		return jr;
	}

	/**
	 * Method for Encrypt Plain String Data
	 * 
	 * @param plainText
	 * @return encryptedText
	 */
	public static String encrypt(String plainText) {
		String encryptedText = "";
		try {
			Cipher cipher = Cipher.getInstance(cipherTransformation);
			byte[] key = encryptionKey.getBytes(characterEncoding);
			SecretKeySpec secretKey = new SecretKeySpec(key, aesEncryptionAlgorithem);
			IvParameterSpec ivparameterspec = new IvParameterSpec(key);
			cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivparameterspec);
			byte[] cipherText = cipher.doFinal(plainText.getBytes("UTF8"));
			Base64.Encoder encoder = Base64.getEncoder();
			encryptedText = encoder.encodeToString(cipherText);

		} catch (Exception E) {
			//System.err.println("Encrypt Exception : " + E.getMessage());
		}
		return encryptedText;
	}

	/**
	 * Method For Get encryptedText and Decrypted provided String
	 * 
	 * @param encryptedText
	 * @return decryptedText
	 */
	public static String decrypt(String encryptedText) {
		String decryptedText = "";
		try {
			Cipher cipher = Cipher.getInstance(cipherTransformation);
			byte[] key = encryptionKey.getBytes(characterEncoding);
			SecretKeySpec secretKey = new SecretKeySpec(key, aesEncryptionAlgorithem);
			IvParameterSpec ivparameterspec = new IvParameterSpec(key);
			cipher.init(Cipher.DECRYPT_MODE, secretKey, ivparameterspec);
			Base64.Decoder decoder = Base64.getDecoder();
			byte[] cipherText = decoder.decode(encryptedText.getBytes("UTF8"));
			decryptedText = new String(cipher.doFinal(cipherText), "UTF-8");

		} catch (Exception E) {
			//System.err.println("decrypt Exception : " + E.getMessage());
		}
		return decryptedText;
	}
}