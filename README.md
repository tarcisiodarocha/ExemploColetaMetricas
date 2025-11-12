# Metrics Collector (Maven example)

Projeto Maven de exemplo que coleta métricas de um computador (CPU, memória, discos, interfaces de rede)
usando a biblioteca OSHI e serializa o resultado em JSON com Gson.

## Requisitos
- Java 11+ (o `pom.xml` está configurado para Java 11)
- Maven

## Como construir
```bash
mvn compile assembly:single
```
O artefato executável ficará em `target/metrics-collector-1.0.0-jar-with-dependencies.jar`.

## Como executar
Coleta uma amostra única e imprime JSON no stdout:

```bash
java -jar target/metrics-collector-1.0.0-jar-with-dependencies.jar
```

Você também pode salvar a saída:

```bash
java -jar target/metrics-collector-1.0.0-jar-with-dependencies.jar > metrics.json
```

## Observações
- Este exemplo é apenas *coleta de métricas local*. Não possui envio em rede.
- Dependências principais:
  - OSHI (com.github.oshi:oshi-core) — para acesso a métricas do sistema.
  - Gson (com.google.code.gson:gson) — para serialização JSON.
