import com.portal_egressos.portal_egressos_backend.models.Usuario;

@ExtendWith(SpringExtension.class)
@SpringBootTest

public class UsuarioRepositoryTest {
    @Autowired
    UsuarioRepository repository;

    @Test 
    public void deveVerificarSalvarUsuario(){
        //cenário
        Usuario user = Usuario.builder().nome("Teste")
                                        .email("teste@teste.com")
                                        .senha("123").build();
        //ação
        Usuario salvo = repository.save(user);

        //verificação
        Assertions.assertNotNull(salvo);
        Assertions.assertEquals(user.getNome(), salvo.getNome());
        Assertions.assertEquals(user.getEmail(), salvo.getEmail());
        Assertions.assertEquals(user.getSenha(), salvo.getSenha());
        Assertions.assertEquals(user.getNome(), salvo.getNome());
    }
}
