package com.study.security.service;

import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.study.security.domain.Member;
import com.study.security.domain.MemberRole;
import com.study.security.dto.MemberDTO;
import com.study.security.repository.MemberRepository;
import com.study.security.repository.MemberRoleRepository;
import com.study.security.vo.SecurityUser;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private MemberRepository memberRepository;
    
    @Autowired
    private MemberRoleRepository memberRoleRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
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
    
    @Transactional
    public void joinMember(MemberDTO memberDTO) {
        String encryptPassword = passwordEncoder.encode(memberDTO.getPassword());
        memberDTO.setPassword(encryptPassword);
        MemberRole role = memberRoleRepository.findByRoleName(memberDTO.getRoleName()).orElse(null);
        Member member = new Member(memberDTO.getId(), encryptPassword, memberDTO.getUsername(), role);
        memberRepository.save(member);
    }
}
