package com.portal_egressos.portal_egressos_backend.service;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class NoticiaServiceTest {
    
    public void deveSalvarNoticia(){

    }

    public void deveGerarErroAoTentarSalvarNoticiaNula(){

    }

    public void deveGerarErroAoTentarSalvarNoticiaSemTitulo(){

    }

    public void deveGerarErroAoTentarSalvarNoticiaSemDescricao(){

    }

    public void deveGerarErroAoTentarSalvarNoticiaSemDataPublicacao(){

    }

    public void deveGerarErroAoTentarSalvarNoticiaSemDataExtracao(){
        
    }

    public void deveGerarErroAoTentarSalvarNoticiaSemLinkNoticia(){

    }

    public void deveGerarErroAoTentarSalvarNoticiaSemStatus(){

    }

    public void deveObterListaNoticias(){

    }

    public void deveObterListaNoticiasVaziaQuandoNaoHouver(){

    }

    public void deveObterListaNoticiasAprovadas(){

    }

    public void deveObterListaNoticiasAprovadasVaziaQuandoNaoHouver(){

    }

    public void deveAtualizarStatusNoticiaParaAprovado(){

    }
    public void deveGerarErroAoTentarAtualizarStatusDeNoticiaNula(){

    }

    public void deveObterListaNoticiasUltimos30dias(){

    }

    public void deveObterListaNoticiasUltimos30diasVaziaQuandoNaoHouver(){

    }

    public void deveRemoverNoticia(){

    }

    public void deveGerarErroAoTentarRemoverNoticiaNula(){

    }

}
