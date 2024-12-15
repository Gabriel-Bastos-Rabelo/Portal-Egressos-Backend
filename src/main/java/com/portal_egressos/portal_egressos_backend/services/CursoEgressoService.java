package com.portal_egressos.portal_egressos_backend.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.portal_egressos.portal_egressos_backend.models.CursoEgresso;
import com.portal_egressos.portal_egressos_backend.repositories.CursoEgressoRepository;

import jakarta.transaction.Transactional;

@Service 
public class CursoEgressoService {

    @Autowired
    private CursoEgressoRepository cursoEgressoRepo;

    @Transactional 
    public CursoEgresso salvar (CursoEgresso cursoEgresso){
        verificarCursoEgresso(cursoEgresso);
        return cursoEgressoRepo.save(cursoEgresso);
    }

    @Transactional 
    public CursoEgresso atualizar(CursoEgresso cursoEgresso){
        verificarCursoEgresso(cursoEgresso);
        verificarId(cursoEgresso);
        return cursoEgressoRepo.save(cursoEgresso);
    }

    @Transactional 
    public void  remover (CursoEgresso cursoEgresso){
        verificarId(cursoEgresso);
        cursoEgressoRepo.delete(cursoEgresso);
    }

    @Transactional 
    public Optional<CursoEgresso> buscarPorId(Long id){
        return cursoEgressoRepo.findById(id);
    }

    public void verificarId(CursoEgresso cursoEgresso){
        if((cursoEgresso == null) || (cursoEgresso.getId() == null) || (!cursoEgressoRepo.existsById(cursoEgresso.getId())))
            throw new IllegalArgumentException("ID inválido!");
    }

    public void verificarCursoEgresso ( CursoEgresso cursoEgresso){
        if (cursoEgresso == null)
            throw new IllegalArgumentException("Curso informado é inválido!");
        if ((cursoEgresso.getAnoInicio() == null) || (cursoEgresso.getAnoInicio().equals(" ")))
            throw new IllegalArgumentException("Ano de início do curso deve ser informada!");
        if ((cursoEgresso.getAnoFim() == null) || (cursoEgresso.getAnoFim().equals("")))
            throw new IllegalArgumentException("Ano de conclusão do curso deve ser informada!");
    }
    
}
