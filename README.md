
<h1>HubSpot Integration API</h1>
<p>Projeto desenvolvido para integrar uma API Java com o HubSpot utilizando autenticação via OAuth 2.0 (Authorization Code Flow), envio de dados para a API do HubSpot e recebimento de eventos via Webhooks.</p>
<hr/>
<h2>💡 Tecnologias utilizadas</h2>
<h3>Java 17 + Spring Boot 3</h3>
<ul>
  <li><strong>Spring Boot</strong>: framework robusto e produtivo para aplicações Java modernas.</li>
  <li><strong>Spring Web</strong>: para criação de endpoints REST.</li>
  <li><strong>Spring Security</strong>: utilizado apenas para desabilitar autenticação padrão e permitir acesso livre à API.</li>
  <li><strong>Validation</strong>: para validação de inputs.</li>
  <li><strong>RestTemplate</strong>: para chamadas HTTP externas (HubSpot API).</li>
  <li><strong>Lombok</strong>: evita código boilerplate (getters/setters, construtores, etc).</li>
</ul>
<p><em>Optei por não usar WebClient pois a simplicidade do projeto favorece RestTemplate. A estrutura também foi mantida limpa, sem sobrecarga de dependências.</em></p>
<hr/>
<h2>🚀 Como rodar o projeto</h2>
<h3>1. Clonar o repositório</h3>
<pre><code>git clone https://github.com/felipelago/HubspotApplication.git
cd HubspotApplication
</code></pre>
<h3>2. Configurar o <code>application.properties</code></h3>
<pre><code>hubspot.client-id=SUA_CLIENT_ID
hubspot.client-secret=SEU_CLIENT_SECRET
hubspot.redirect-uri=http://localhost:8080/oauth/callback
hubspot.scope=crm.objects.contacts.write crm.objects.contacts.read
hubspot.auth-url=https://app.hubspot.com/oauth/authorize
hubspot.token-url=https://api.hubapi.com/oauth/v1/token
</code></pre>
<h3>3. Rodar a aplicação</h3>
<pre><code>./mvnw spring-boot:run
</code></pre>
<h3>4. (Opcional) Expor localmente com ngrok (para webhooks)</h3>
Como meu ip público é privado e não possuo ip fixo optei por utilizar o ngrok que é uma ferramenta que resolve esse problema.
Você pode baixar o ngrok através do link: https://dashboard.ngrok.com/
Utilize qualquer conta para login e após instalado, rode o comando abaixo para expor seu ip local na porta 8080.
<pre><code>ngrok http 8080</code></pre>
<hr/>
<h2>🔑 Autenticação OAuth 2.0 com HubSpot</h2>
<h3>1. Gerar URL de autorização</h3>
Faça uma requisição para:
<pre><code>GET /oauth/authorize-url</code></pre>
A resposta será uma URL do HubSpot que você deve abrir no navegador para autorizar o app.
<h3>2. Autorizar e obter o <code>code</code></h3>
Ao acessar a URL, o HubSpot exibirá uma tela para o usuário aceitar os escopos solicitados.
Após a autorização, o usuário será redirecionado para:
<pre><code>http://localhost:8080/oauth/callback?code=SEU_CODE</code></pre>
<h3>3. Trocar <code>code</code> pelo token</h3>
Esse redirecionamento é tratado automaticamente pelo endpoint:
<pre><code>GET /oauth/callback</code></pre>
O controller realiza a troca do code por um access_token e um refresh_token através de uma chamada HTTP para o HubSpot.

Exemplo de resposta:
<pre><code>{
  "access_token": "...",
  "refresh_token": "..."
}</code></pre>
<hr/>
<h2>📧 Enviar contato para o HubSpot</h2>
<pre><code>POST /contacts</code></pre>
<pre><code>Authorization: Bearer SEU_ACCESS_TOKEN
Content-Type: application/json</code></pre>
<pre><code>{
  "email": "joao@email.com",
  "firstname": "João",
  "lastname": "Silva"
}</code></pre>
<hr/>
<h2>🔔 Webhook - Recebimento de eventos</h2>
<pre><code>POST /webhooks/hubspot</code></pre>
<pre><code>[
  {
    "subscriptionType": "contact.creation",
    "objectId": 123456789,
    "occurredAt": 1712444400000
  }
]</code></pre>
<blockquote><strong>Importante:</strong> o webhook só funciona com URL pública (como <code>ngrok</code>) e se estiver corretamente configurado no app.</blockquote>
<hr/>
<h2>💡 Decisões tomadas & melhorias futuras</h2>
<ul>
  <li>Evitar sobrecarga de abstração (sem usar WebClient ou WebFlux).</li>
  <li>Controllers simples, focados na lógica de integração.</li>
  <li>Utilização de DTOs para organização de entrada/saída.</li>
  <li>Spring Security desabilitado para não interferir no fluxo OAuth.</li>
</ul>
<h3>Melhorias futuras:</h3>
<ul>
  <li>Persistência dos tokens em banco (com refresh automático)</li>
  <li>Armazenamento dos logs de webhook em banco</li>
  <li>Testes unitários e de integração</li>
  <li>Deploy da API em nuvem</li>
</ul>
