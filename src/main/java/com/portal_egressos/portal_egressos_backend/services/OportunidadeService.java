package com.portal_egressos.portal_egressos_backend.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.portal_egressos.portal_egressos_backend.enums.Status;
import com.portal_egressos.portal_egressos_backend.exceptions.RegraNegocioRunTime;
import com.portal_egressos.portal_egressos_backend.models.Oportunidade;
import com.portal_egressos.portal_egressos_backend.repositories.OportunidadeRepository;

import jakarta.transaction.Transactional;

@Service
public class OportunidadeService {

    @Autowired
    private OportunidadeRepository oportunidadeRepositorio;

    @Transactional
    public Oportunidade salvarOportunidade(Oportunidade oportunidade) {
        if (oportunidade == null) {
            throw new RegraNegocioRunTime("A oportunidade não pode ser nula.");
        }
        validarCamposObrigatorios(oportunidade);
        return oportunidadeRepositorio.save(oportunidade);
    }

    @Transactional
    public Oportunidade atualizarOportunidade(Oportunidade oportunidadeAtualizada) {
        verificarOportunidadeId(oportunidadeAtualizada);
        Oportunidade oportunidadeExistente = oportunidadeRepositorio.findById(oportunidadeAtualizada.getId()).get();

        if(!oportunidadeAtualizada.getTitulo().isEmpty()){
            oportunidadeExistente.setTitulo(oportunidadeAtualizada.getTitulo());
        }
        if(!oportunidadeAtualizada.getDescricao().isEmpty()){
            oportunidadeExistente.setDescricao(oportunidadeAtualizada.getDescricao());
        }
        if(!oportunidadeAtualizada.getLocal().isEmpty()){
            oportunidadeExistente.setLocal(oportunidadeAtualizada.getLocal());
        }
        if(!oportunidadeAtualizada.getTipo().isEmpty()){
            oportunidadeExistente.setTipo(oportunidadeAtualizada.getTipo());
        }
        if(oportunidadeAtualizada.getDataPublicacao() != null){
            oportunidadeExistente.setDataPublicacao(oportunidadeAtualizada.getDataPublicacao());
        }
        if(oportunidadeAtualizada.getDataExpiracao() != null){
            oportunidadeExistente.setDataExpiracao(oportunidadeAtualizada.getDataExpiracao());
        }
        if(oportunidadeAtualizada.getSalario() != null){
            oportunidadeExistente.setSalario(oportunidadeAtualizada.getSalario());
        }
        if(!oportunidadeAtualizada.getLink().isEmpty()){
            oportunidadeExistente.setLink(oportunidadeAtualizada.getLink());
        }
        if(oportunidadeAtualizada.getStatus() != null){
            oportunidadeExistente.setStatus(oportunidadeAtualizada.getStatus());
        }

        return oportunidadeRepositorio.save(oportunidadeExistente);
    }

    // Remover Oportunidade
    @Transactional
    public void removerOportunidade(Oportunidade oportunidade) {
        verificarOportunidadeId(oportunidade);
        oportunidadeRepositorio.delete(oportunidade);
    }


    public Oportunidade verificarOportunidadeId(Oportunidade oportunidade) {
        if ((oportunidade == null) || (oportunidade.getId() == null)
                || !(oportunidadeRepositorio.existsById(oportunidade.getId()))) {
            throw new RegraNegocioRunTime("ID de oportunidade é inválido.");
        }
        return oportunidadeRepositorio.findById(oportunidade.getId()).get();
    }

    public Oportunidade buscarOportunidadePorId(Long id) {
        if (id == null) {
            throw new RegraNegocioRunTime("O ID da oportunidade deve ser informado.");
        }
        return oportunidadeRepositorio.findById(id)
                .orElseThrow(() -> new RegraNegocioRunTime("Oportunidade com ID " + id + " não encontrada."));
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
    public List<Oportunidade> listarTodasOportunidadesOrdenadasPorData() {
        List<Oportunidade> oportunidades = oportunidadeRepositorio.findAllByOrderByDataPublicacaoDesc();
        if (oportunidades.isEmpty()) {
            throw new RegraNegocioRunTime("Nenhuma oportunidade encontrada.");
        }
        return oportunidades;
    }

    public List<Oportunidade> listarOportunidadesAprovadasOrdenadasPorData() {
        List<Oportunidade> oportunidades = oportunidadeRepositorio.findAllByStatusOrderByDataPublicacaoDesc(Status.APROVADO);
        if (oportunidades.isEmpty()) {
            throw new RegraNegocioRunTime("Nenhuma oportunidade encontrada com status APROVADO.");
        }
        return oportunidades;
    }

    public List<Oportunidade> listarOportunidadesPendentesOrdenadasPorData() {
        List<Oportunidade> oportunidades = oportunidadeRepositorio.findAllByStatusOrderByDataPublicacaoDesc(Status.PENDENTE);
        if (oportunidades.isEmpty()) {
            throw new RegraNegocioRunTime("Nenhuma oportunidade encontrada com status PENDENTE.");
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
        if (oportunidade.getStatus() == null) {
            throw new RegraNegocioRunTime("O status deve ser informado.");
        }
    }

}
