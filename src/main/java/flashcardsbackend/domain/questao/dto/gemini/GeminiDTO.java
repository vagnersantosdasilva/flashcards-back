package flashcardsbackend.domain.questao.dto.gemini;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Data
public class GeminiDTO {
    private List<MensagemDTO> contents = new ArrayList<>();

}


/*
*
* {
   "contents":[
      {
         "role":"user",
         "parts":[
            {
               "text":"Comandos e Explicações:Comando: docker buildExplicação: Este comando é utilizado para construir uma nova imagem de contêiner a partir de um Dockerfile. Ele compila as instruções do Dockerfile e cria uma imagem que pode ser utilizada para instanciar contêineres.Comando: docker image lsExplicação: Mostra uma lista de todas as imagens de contêiner disponíveis no sistema. É útil para verificar quais imagens foram construídas ou baixadas localmente.Comando: docker run -p 8080:8080Explicação: Inicia um contêiner em primeiro plano. O parâmetro -p mapeia a porta 8080 do host para a porta 8080 do contêiner, permitindo acesso ao aplicativo do contêiner no host.Comando: docker run -dExplicação: Inicia um contêiner em segundo plano (modo desanexado). O contêiner é executado em segundo plano, deixando o terminal livre para outros comandos.Comando: docker psExplicação: Lista os contêineres em execução no momento. Fornece informações como ID, nome e status do contêiner.Comando: docker exec -it 52 shExplicação: Acessa um shell dentro de um contêiner em execução. O número 52 é o nome ou ID do contêiner. Os parâmetros -it permitem interação interativa com o shell.Comando: docker stop '<nome_do_contêiner>'Explicação: Para a execução de um contêiner. Substitua '<nome_do_contêiner>' pelo nome ou ID do contêiner que deseja parar.Comando: docker rm '<nome_do_contêiner>'Explicação: Remove um contêiner. Substitua '<nome_do_contêiner>' pelo nome ou ID do contêiner que deseja remover. É necessário parar o contêiner antes de removê-lo.Comando: docker run -e MESSAGE_COLOR=corExplicação: Passa uma variável de ambiente durante a inicialização do contêiner. Substitua cor pela cor desejada e MESSAGE_COLOR pela variável de ambiente usada no aplicativo.Comando: docker run -v -caminho-no-host:-caminho-no-contêiner nome_da_imagemExplicação: Cria um bind mount para compartilhar recursos do sistema de arquivos do host com o contêiner. O primeiro caminho é o local no host, o segundo caminho é o local no contêiner, e nome_da_imagem é o nome da imagem do contêiner a ser iniciado.Esses comandos são essenciais para a construção, execução e gerenciamento de contêineres Docker, permitindo a criação de ambientes isolados e a implantação de aplicações de maneira eficiente."
            }
         ]
      }
   ]
}
* */