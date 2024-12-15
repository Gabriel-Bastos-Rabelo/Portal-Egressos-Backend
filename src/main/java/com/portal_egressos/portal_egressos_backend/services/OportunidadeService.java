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
    private OportunidadeRepository oportunidadeRepository;

    public Oportunidade salvarOportunidade(Oportunidade oportunidade) {
        validarCamposObrigatorios(oportunidade);
        return oportunidadeRepository.save(oportunidade);
    }

    public Oportunidade atualizarOportunidade(Long id, Oportunidade oportunidadeAtualizada) {
        Optional<Oportunidade> oportunidadeExistente = oportunidadeRepository.findById(id);
        if (oportunidadeExistente.isPresent()) {
            Oportunidade oportunidade = oportunidadeExistente.get();
            oportunidade.setTitulo(oportunidadeAtualizada.getTitulo());
            oportunidade.setDescricao(oportunidadeAtualizada.getDescricao());
            oportunidade.setLocal(oportunidadeAtualizada.getLocal());
            oportunidade.setTipo(oportunidadeAtualizada.getTipo());
            oportunidade.setDataPublicacao(oportunidadeAtualizada.getDataPublicacao());
            oportunidade.setDataExpiracao(oportunidadeAtualizada.getDataExpiracao());
            oportunidade.setSalario(oportunidadeAtualizada.getSalario());
            oportunidade.setLink(oportunidadeAtualizada.getLink());
            oportunidade.setStatus(oportunidadeAtualizada.getStatus());
            return oportunidadeRepository.save(oportunidade);
        } else {
            throw new RegraNegocioRunTime("Oportunidade não encontrada");
        }
    }

    // Remover Oportunidade
    public void removerOportunidade(Long id) {
        if (oportunidadeRepository.existsById(id)) {
            oportunidadeRepository.deleteById(id);
        } else {
            throw new RegraNegocioRunTime("Oportunidade não encontrada com o ID: " + id);
        }
    }

    // Verificar Oportunidade por ID
    public Oportunidade verificarOportunidade(Long id) {
        return oportunidadeRepository.findById(id)
                .orElseThrow(() -> new RegraNegocioRunTime("Oportunidade não encontrada com o ID: " + id));
    }

    // Verificar Oportunidade por título
    public List<Oportunidade> buscarPorTitulo(String titulo) {
        List<Oportunidade> oportunidades = oportunidadeRepository.findByTituloContaining(titulo);
        if (oportunidades.isEmpty()) {
            throw new RegraNegocioRunTime("Nenhuma oportunidade encontrada com o título: " + titulo);
        }
        return oportunidades;
    }

    // Verificar Oportunidade por Data
    public List<Oportunidade> listarOportunidadesOrdenadasPorData() {
        List<Oportunidade> oportunidades = oportunidadeRepository.findAllByOrderByDataPublicacaoDesc();
        if (oportunidades.isEmpty()) {
            throw new RegraNegocioRunTime("Nenhuma oportunidade encontrada.");
        }
        return oportunidades;
    }

    // Método para buscar oportunidades pelo nome do egresso
    public List<Oportunidade> buscarPorNomeEgresso(String nome) {
        List<Oportunidade> oportunidades = oportunidadeRepository.findByEgressoNomeContaining(nome);
        if (oportunidades.isEmpty()) {
            throw new RegraNegocioRunTime("Nenhuma oportunidade encontrada para o egresso com o nome: " + nome);
        }
        return oportunidades;
    }

    // Método validar os campos obrigatórios
    private void validarCamposObrigatorios(Oportunidade oportunidade) {
        if (oportunidade.getEgresso() == null) {
            throw new RegraNegocioRunTime("O campo 'egresso' é obrigatório.");
        }
        if (oportunidade.getTitulo() == null || oportunidade.getTitulo().isEmpty()) {
            throw new RegraNegocioRunTime("O campo 'titulo' é obrigatório.");
        }
        if (oportunidade.getDescricao() == null || oportunidade.getDescricao().isEmpty()) {
            throw new RegraNegocioRunTime("O campo 'descricao' é obrigatório.");
        }
        if (oportunidade.getLocal() == null || oportunidade.getLocal().isEmpty()) {
            throw new RegraNegocioRunTime("O campo 'local' é obrigatório.");
        }
        if (oportunidade.getTipo() == null || oportunidade.getTipo().isEmpty()) {
            throw new RegraNegocioRunTime("O campo 'tipo' é obrigatório.");
        }
        if (oportunidade.getDataPublicacao() == null) {
            throw new RegraNegocioRunTime("O campo 'dataPublicacao' é obrigatório.");
        }
        if (oportunidade.getStatus() == null || oportunidade.getStatus().isEmpty()) {
            throw new RegraNegocioRunTime("O campo 'status' é obrigatório.");
        }
    }

}
