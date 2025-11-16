📦 EventBridgeProducer

Aplicação Java (Java 17 + Spring) responsável por receber requisições REST, extrair o campo status e publicar eventos filtrados no AWS EventBridge, que direciona cada evento para uma fila SQS correspondente.

Este serviço atua como o producer da arquitetura orientada a eventos.

🚀 Arquitetura

Fluxo principal:

Cliente envia uma requisição HTTP para o serviço
→ /event

A aplicação extrai o campo status do payload

Com base nesse status, constrói um evento padronizado

Envia o evento para o EventBridge com o filtro definido por status

O EventBridge roteia o evento para a SQS correspondente, conforme regras configuradas nos Event Rules

Summary:

REST Request → Event Producer (Java/Spring) → EventBridge Rule (status filter) → SQS Queue

🛠 Tecnologias utilizadas

Java 17

Spring Boot

AWS SDK for Java v2

Amazon EventBridge

Amazon SQS

Maven

Docker (opcional)

📁 Estrutura do Projeto
src/
 ├─ main/java
 │   ├─ controller/        # Endpoint REST
 │   ├─ service/           # Regras de negócio / montagem do evento
 │   ├─ producer/          # Publicação para EventBridge
 │   └─ config/            # Beans AWS e configs
 └─ main/resources
     └─ application.yml

🔗 Endpoint
POST /event

Envia um evento para o EventBridge com base no status.

Exemplo de requisição
{
  "id": "123",
  "status": "APPROVED",
  "payload": {
    "amount": 100.50,
    "customer": "JOAO"
  }
}


O campo status será usado como Event Pattern no EventBridge.

🧩 Exemplo de evento enviado para o EventBridge
{
  "Source": "ms-checkout",
  "DetailType": "CheckoutStatusEvent",
  "Detail": "{\"id\": \"123\", \"status\": \"APPROVED\", \"payload\": {...}}",
  "EventBusName": "checkout-event-bus"
}

⚙️ Configuração AWS

A aplicação usa as credenciais padrão da AWS CLI.

Execute:

aws configure


Ou configure variáveis de ambiente:

AWS_ACCESS_KEY_ID=xxxx
AWS_SECRET_ACCESS_KEY=xxxx
AWS_REGION=us-east-1

📄 application.yml

Exemplo de configuração:

aws:
  eventbridge:
    bus-name: checkout-event-bus

▶️ Executando localmente
1. Build
mvn clean package

2. Rodar aplicação
mvn spring-boot:run


ou

java -jar target/ms-checkout-event-producer.jar

🧪 Testando

Faça uma requisição:

curl -X POST http://localhost:8080/event \
  -H "Content-Type: application/json" \
  -d '{
        "id":"123",
        "status":"APPROVED",
        "payload":{"amount":100.50}
      }'


Verifique no EventBridge → Monitoring e na SQS configurada.

📬 Filtragem no EventBridge

No Event Rule:

{
  "detail": {
    "status": ["APPROVED"]
  }
}


Cada status pode ter sua própria fila:

APPROVED → SQS: queue-approved

REJECTED → SQS: queue-rejected

etc.

🧱 Possíveis melhorias

Retentativa de envio (Retry Policy)

DLQ em caso de falha no EventBridge

Observabilidade com CloudWatch Metrics

Resiliência com Circuit Breaker

👥 Contribuindo

Pull requests são bem-vindos!
Aberturas de issues também.
