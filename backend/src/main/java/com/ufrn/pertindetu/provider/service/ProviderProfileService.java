package com.ufrn.pertindetu.provider.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ufrn.pertindetu.base.mappers.DtoMapper;
import com.ufrn.pertindetu.base.repository.GenericRepository;
import com.ufrn.pertindetu.base.service.GenericService;
import com.ufrn.pertindetu.base.utils.exception.BusinessException;
import com.ufrn.pertindetu.provider.dto.ProviderProfileDTO;
import com.ufrn.pertindetu.provider.mapper.ProviderProfileMapper;
import com.ufrn.pertindetu.provider.model.ProviderProfile;
import com.ufrn.pertindetu.provider.repository.ProviderProfileRepository;
import com.ufrn.pertindetu.user.model.User;
import com.ufrn.pertindetu.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProviderProfileService
        implements GenericService<ProviderProfile, ProviderProfileDTO> {

    private final ProviderProfileRepository providerRepository;
    private final ProviderProfileMapper mapper;
    private final UserRepository userRepository;

    @Override
    public GenericRepository<ProviderProfile> getRepository() {
        return providerRepository;
    }

    @Override
    public DtoMapper<ProviderProfile, ProviderProfileDTO> getDtoMapper() {
        return mapper;
    }
    
    @Transactional
    public ProviderProfileDTO createOrUpdate(String userEmail,
                                             ProviderProfileDTO dto) {
        User user = userRepository.findByEmailAndActiveTrue(userEmail)
                .orElseThrow(() -> new BusinessException(
                        "Usuário não encontrado.", HttpStatus.NOT_FOUND));

        if (!user.getRole().name().equals("PROVIDER")) {
            throw new BusinessException(
                    "Apenas usuários com perfil de Prestador podem criar uma "
                            + "vitrine profissional.",
                    HttpStatus.FORBIDDEN);
        }

        ProviderProfile profile = providerRepository.findByUserId(user.getId())
                .orElseGet(ProviderProfile::new);

        profile.setUser(user);
        profile.setCategory(dto.getCategory());
        profile.setBio(dto.getBio());
        profile.setWhatsappPhone(dto.getWhatsappPhone());
        profile.setNeighborhood(dto.getNeighborhood());
        profile.setCity(dto.getCity());
        profile.setState(dto.getState().toUpperCase());

        return mapper.toDto(providerRepository.save(profile));
    }

    @Transactional(readOnly = true)
    public ProviderProfileDTO getMyProfile(String userEmail) {
        User user = userRepository.findByEmailAndActiveTrue(userEmail)
                .orElseThrow(() -> new BusinessException(
                        "Usuário não encontrado.", HttpStatus.NOT_FOUND));

        ProviderProfile profile = providerRepository.findByUserId(user.getId())
                .orElseThrow(() -> new BusinessException(
                        "Você ainda não criou sua vitrine profissional.",
                        HttpStatus.NOT_FOUND));

        return mapper.toDto(profile);
    }
}