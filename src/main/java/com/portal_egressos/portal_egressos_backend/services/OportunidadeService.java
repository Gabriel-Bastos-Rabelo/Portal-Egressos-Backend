package com.portal_egressos.portal_egressos_backend.services;

import com.portal_egressos.portal_egressos_backend.exceptions.RegraNegocioRunTime;
import com.portal_egressos.portal_egressos_backend.models.Oportunidade;
import com.portal_egressos.portal_egressos_backend.repositories.OportunidadeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OportunidadeService {

    @Autowired
    private OportunidadeRepository oportunidadeRepositorio;

    public Oportunidade salvarOportunidade(Oportunidade oportunidade) {
        validarCamposObrigatorios(oportunidade);
        return oportunidadeRepositorio.save(oportunidade);
    }

    public Oportunidade atualizarOportunidade(Long id, Oportunidade oportunidadeAtualizada) {
        Optional<Oportunidade> oportunidadeExistente = oportunidadeRepositorio.findById(id);

        if (oportunidadeExistente.isPresent()) {
            Oportunidade oportunidade = oportunidadeExistente.get();
            if(!oportunidadeAtualizada.getTitulo().isEmpty()){
                oportunidade.setTitulo(oportunidadeAtualizada.getTitulo());
            }
            if(!oportunidadeAtualizada.getDescricao().isEmpty()){
                oportunidade.setDescricao(oportunidadeAtualizada.getDescricao());
            }
            if(!oportunidadeAtualizada.getLocal().isEmpty()){
                oportunidade.setLocal(oportunidadeAtualizada.getLocal());
            }
            if(!oportunidadeAtualizada.getTipo().isEmpty()){
                oportunidade.setTipo(oportunidadeAtualizada.getTipo());
            }
            if(oportunidadeAtualizada.getDataPublicacao() != null){
                oportunidade.setDataPublicacao(oportunidadeAtualizada.getDataPublicacao());
            }
            if(oportunidadeAtualizada.getDataExpiracao() != null){
                oportunidade.setDataExpiracao(oportunidadeAtualizada.getDataExpiracao());
            }
            if(oportunidadeAtualizada.getSalario() != null){
                oportunidade.setSalario(oportunidadeAtualizada.getSalario());
            }
            if(!oportunidadeAtualizada.getLink().isEmpty()){
                oportunidade.setLink(oportunidadeAtualizada.getLink());
            }
            if(!oportunidadeAtualizada.getStatus().isEmpty()){
                oportunidade.setStatus(oportunidadeAtualizada.getStatus());
            }

            return oportunidadeRepositorio.save(oportunidade);
        } else {
            throw new RegraNegocioRunTime("Oportunidade não encontrada.");
        }
    }

    // Remover Oportunidade
    public void removerOportunidade(Long id) {
        if (oportunidadeRepositorio.existsById(id)) {
            oportunidadeRepositorio.deleteById(id);
        } else {
            throw new RegraNegocioRunTime("Oportunidade não encontrada com o ID: " + id);
        }
    }

    // Verificar Oportunidade por ID
    public Oportunidade verificarOportunidade(Long id) {
        return oportunidadeRepositorio.findById(id)
                .orElseThrow(() -> new RegraNegocioRunTime("Oportunidade não encontrada com o ID: " + id));
    }

    // Verificar Oportunidade por título
    public List<Oportunidade> buscarPorTitulo(String titulo) {
        List<Oportunidade> oportunidades = oportunidadeRepositorio.findByTituloContaining(titulo);
        if (oportunidades.isEmpty()) {
            throw new RegraNegocioRunTime("Nenhuma oportunidade encontrada com o título: " + titulo);
        }
        return oportunidades;
    }

    // Verificar Oportunidade por Data
    public List<Oportunidade> listarOportunidadesOrdenadasPorData() {
        List<Oportunidade> oportunidades = oportunidadeRepositorio.findAllByOrderByDataPublicacaoDesc();
        if (oportunidades.isEmpty()) {
            throw new RegraNegocioRunTime("Nenhuma oportunidade encontrada.");
        }
        return oportunidades;
    }

    // Método para buscar oportunidades pelo nome do egresso
    public List<Oportunidade> buscarPorNomeEgresso(String nome) {
        List<Oportunidade> oportunidades = oportunidadeRepositorio.findByEgressoNomeContaining(nome);
        if (oportunidades.isEmpty()) {
            throw new RegraNegocioRunTime("Nenhuma oportunidade encontrada para o egresso com o nome: " + nome);
        }
        return oportunidades;
    }

    // Método validar os campos obrigatórios
    private void validarCamposObrigatorios(Oportunidade oportunidade) {
        if (oportunidade.getEgresso() == null) {
            throw new RegraNegocioRunTime("O egresso deve ser informado.");
        }
        if (oportunidade.getTitulo() == null || oportunidade.getTitulo().isEmpty()) {
            throw new RegraNegocioRunTime("O titulo deve ser informado.");
        }
        if (oportunidade.getDescricao() == null || oportunidade.getDescricao().isEmpty()) {
            throw new RegraNegocioRunTime("A descricao deve ser informado.");
        }
        if (oportunidade.getLocal() == null || oportunidade.getLocal().isEmpty()) {
            throw new RegraNegocioRunTime("O local deve ser informado.");
        }
        if (oportunidade.getTipo() == null || oportunidade.getTipo().isEmpty()) {
            throw new RegraNegocioRunTime("O tipo deve ser informado.");
        }
        if (oportunidade.getDataPublicacao() == null) {
            throw new RegraNegocioRunTime("A data da publicacao deve ser informado.");
        }
        if (oportunidade.getStatus() == null || oportunidade.getStatus().isEmpty()) {
            throw new RegraNegocioRunTime("O status deve ser informado.");
        }
    }

}
