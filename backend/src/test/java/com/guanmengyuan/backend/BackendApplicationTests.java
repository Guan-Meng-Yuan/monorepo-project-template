package com.guanmengyuan.backend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import cn.hutool.v7.crypto.digest.BCrypt;

@SpringBootTest
class BackendApplicationTests {

	@Test
	void contextLoads() {
		System.out.println(BCrypt.hashpw("123456"));
	}

}
