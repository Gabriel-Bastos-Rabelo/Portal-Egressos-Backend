package com.portal_egressos.portal_egressos_backend.services;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.portal_egressos.portal_egressos_backend.exceptions.RegraNegocioRunTime;
import com.portal_egressos.portal_egressos_backend.models.Egresso;
import com.portal_egressos.portal_egressos_backend.repositories.EgressoRepository;

@Service
public class EgressoService {

    @Autowired
    private EgressoRepository egressoRepositorio;

    private static final String EMAIL_PATTERN = 
        "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    private static final String LINKEDIN_PATTERN = 
    "^(https?:\\/\\/)?(www\\.)?linkedin\\.com\\/in\\/[A-Za-z0-9\\-_.]+$";

    private static final String INSTAGRAM_PATTERN = 
    "^(https?:\\/\\/)?(www\\.)?instagram\\.com\\/[A-Za-z0-9_.]+$";


    private static final Pattern emailPattern = Pattern.compile(EMAIL_PATTERN, Pattern.CASE_INSENSITIVE);
    private static final Pattern linkedinPattern = Pattern.compile(LINKEDIN_PATTERN, Pattern.CASE_INSENSITIVE);
    private static final Pattern instagramPattern = Pattern.compile(INSTAGRAM_PATTERN, Pattern.CASE_INSENSITIVE);


    @Transactional
    public Egresso salvarEgresso(Egresso egresso) {
        verificarEgresso(egresso);
        Optional<Egresso> egressoExistente = egressoRepositorio.findByUsuarioEmail(egresso.getUsuario().getEmail());
        if (egressoExistente.isPresent()) {
            throw new RegraNegocioRunTime(String.format("Egresso com email '%s' já existe.", egresso.getUsuario().getEmail()));
        }

        String senhaEncriptada = new BCryptPasswordEncoder().encode(egresso.getUsuario().getSenha());

        egresso.getUsuario().setSenha(senhaEncriptada);
        return egressoRepositorio.save(egresso);
    }

    public List<Egresso> buscarEgresso(Egresso filtro) {
        Example<Egresso> example = Example.of(filtro, ExampleMatcher.matching()
                .withIgnoreCase()
                .withStringMatcher(StringMatcher.CONTAINING));

        return egressoRepositorio.findAll(example);
    }

    @Transactional
    public Egresso atualizarEgresso(Egresso egresso) {
        verificarEgressoId(egresso);

        Egresso egressoExistente = egressoRepositorio.findById(egresso.getId()).get();

        if(egresso.getUsuario().getSenha() != null && !egresso.getUsuario().getSenha().isEmpty()){
            String senhaEncriptada = new BCryptPasswordEncoder().encode(egresso.getUsuario().getSenha());
            egressoExistente.getUsuario().setSenha(senhaEncriptada);
        }

        if (egresso.getNome() != null && !egresso.getNome().isEmpty()){
            egressoExistente.setNome(egresso.getNome());
        }
        if (egresso.getDescricao() != null && !egresso.getDescricao().isEmpty()){
            egressoExistente.setDescricao(egresso.getDescricao());
        }
        if (egresso.getFoto() != null && !egresso.getFoto().isEmpty()){
            egressoExistente.setFoto(egresso.getFoto());
        }
        if (egresso.getLinkedin() != null && !egresso.getLinkedin().isEmpty()){
            egressoExistente.setLinkedin(egresso.getLinkedin());
        }
        if (egresso.getInstagram() != null && !egresso.getInstagram().isEmpty()){
            egressoExistente.setInstagram(egresso.getInstagram());
        }
        if (egresso.getCurriculo() != null && !egresso.getCurriculo().isEmpty()){
            egressoExistente.setCurriculo(egresso.getCurriculo());
        }

        return egressoRepositorio.save(egressoExistente);
    }

    @Transactional
    public void removerEgresso(Egresso egresso) {
        verificarEgressoId(egresso);
        egressoRepositorio.delete(egresso);
    }

    public void verificarEgresso(Egresso egresso) {
        if (egresso == null) {
            throw new RegraNegocioRunTime("Egresso inválido.");
        }
        if (egresso.getNome() == null) {
            throw new RegraNegocioRunTime("Nome do egresso deve ser informado.");
        }
        if(egresso.getLinkedin() != null && !validarUrl(egresso.getLinkedin(), linkedinPattern)){
            throw new RegraNegocioRunTime("Linkedin deve ter uma url válida.");
        }
        if(egresso.getInstagram() != null && !validarUrl(egresso.getInstagram(), instagramPattern)){
            throw new RegraNegocioRunTime("Instagram deve ter uma url válida.");
        }
        if(egresso.getUsuario() == null){
            throw new RegraNegocioRunTime("Usuário do egresso deve ser informado");
        }
        if (egresso.getUsuario().getEmail() == null) {
            throw new RegraNegocioRunTime("Email do egresso deve ser informado.");
        }
        if(!validarEmail(egresso.getUsuario().getEmail())){
            throw new RegraNegocioRunTime("Email informado é inválido");
        }
        if(egresso.getUsuario().getSenha() == null){
            throw new RegraNegocioRunTime("Senha do egresso deve ser informado.");
        }
        if(!validarSenha(egresso.getUsuario().getSenha())){
            throw new RegraNegocioRunTime("Senha informada deve ter no mínimo 8 caracteres");
        }

    }

    private boolean validarEmail(String email){
        Matcher matcher = emailPattern.matcher(email);
        return matcher.matches();
    }

    private boolean validarSenha(String senha){
        return senha != null && senha.length() >= 8;
    }

    private boolean validarUrl(String url, Pattern pattern) {
        return url != null && pattern.matcher(url).matches();
    }

    public void verificarEgressoId(Egresso egresso) {
        if ((egresso == null) || (egresso.getId() == null) || !(egressoRepositorio.existsById(egresso.getId()))) {
            throw new RegraNegocioRunTime("ID de egresso é inválido.");
        }
    }

    public List<Egresso> listarEgressos() {
        return egressoRepositorio.findAll();
    }

}
