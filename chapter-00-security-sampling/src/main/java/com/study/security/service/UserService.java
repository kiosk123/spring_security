package com.study.security.service;

import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.study.security.domain.Member;
import com.study.security.repository.MemberRepository;
import com.study.security.vo.SecurityUser;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private MemberRepository memberRepository;
    /**
     * 인증 매니저가 정상 동작하는지 확인하기 위한 간단 테스트용 코드
     */
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        User sampleUser = new User(username, "{noop}1111", Arrays.asList(new SimpleGrantedAuthority("ROLE_MANAGER")));
//        return sampleUser;
//    }
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Member> opt = memberRepository.findById(username);
        return opt.filter(m -> !Objects.isNull(m))
                  .map(m -> new SecurityUser(m))
                  .get();
    }
}
