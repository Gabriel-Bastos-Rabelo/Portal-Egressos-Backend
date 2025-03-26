package com.portal_egressos.portal_egressos_backend.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.portal_egressos.portal_egressos_backend.exceptions.RegraNegocioRunTime;
import com.portal_egressos.portal_egressos_backend.models.CursoEgresso;
import com.portal_egressos.portal_egressos_backend.repositories.CursoEgressoRepository;

import jakarta.transaction.Transactional;

@Service 
public class CursoEgressoService {

    @Autowired
    private CursoEgressoRepository cursoEgressoRepo;

    @Transactional 
    public CursoEgresso salvar(CursoEgresso cursoEgresso){
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
    public void  remover(CursoEgresso cursoEgresso){
        verificarId(cursoEgresso);
        cursoEgressoRepo.delete(cursoEgresso);
    }

    public Optional<CursoEgresso> buscarPorId(Long id){
        return cursoEgressoRepo.findById(id);
    }

    public Optional<CursoEgresso> buscarPorEgressoId(Long egressoId) {
        return cursoEgressoRepo.findByEgressoId(egressoId);
    }
    

    public void verificarId(CursoEgresso cursoEgresso){
        if((cursoEgresso == null) || (cursoEgresso.getId() == null) || (!cursoEgressoRepo.existsById(cursoEgresso.getId())))
            throw new RegraNegocioRunTime("ID inválido.");
    }

    public void verificarCursoEgresso ( CursoEgresso cursoEgresso){
        if (cursoEgresso == null)
            throw new RegraNegocioRunTime("Curso informado é inválido.");
        if ((cursoEgresso.getAnoInicio() == null) || (cursoEgresso.getAnoInicio() <= 0))
            throw new RegraNegocioRunTime("Ano de início do curso deve ser informado.");
        if ((cursoEgresso.getAnoFim() == null) || (cursoEgresso.getAnoFim() <= 0))
            throw new RegraNegocioRunTime("Ano de conclusão do curso deve ser informado.");
        if (cursoEgresso.getAnoFim() < cursoEgresso.getAnoInicio()) 
            throw new RegraNegocioRunTime("Ano de conclusão não pode ser anterior ao ano de início.");
            
    }
    
}
