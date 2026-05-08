<!-- DECISION-CNST-8D7300 -->
## Decision: Prohibit MongoDB and mandate PostgreSQL for core pipelines

**Status**: Active  
**Date**: 2026-04-22  
**Severity**: Critical

**Files**:
- `infrastructure/database`
- `src/db/config.ts`

**Rules**:
```json
{
  "conditions": [
    {
      "type": "file",
      "pattern": "{infrastructure/database/**,src/db/config.ts}",
      "content_rules": [
        {
          "mode": "string",
          "patterns": [
            "mongodb",
            "mongoose",
            "mongo"
          ]
        }
      ]
    }
  ],
  "match_mode": "all"
}
```

### Context

**Problem:** Need to define and enforce the database technology stack to ensure system consistency and data integrity.

**Decision:** The core pipeline must exclusively use PostgreSQL 16 with pgvector and Redis; the use of MongoDB is strictly prohibited.

**Rationale:** Enforcing a specific database stack ensures architectural consistency, simplifies maintenance, and leverages existing infrastructure and expertise with PostgreSQL and pgvector.

**Alternatives Considered:**
- **MongoDB**: Prohibited to maintain stack consistency and data integrity requirements.

---

<!-- DECISION-CNST-393329 -->
## Decision: Prohibition of MongoDB in the Tech Stack for Analytics Events

**Status**: Active  
**Date**: 2026-04-18  
**Severity**: Critical

**Rules**:
```json
{
  "conditions": [
    {
      "type": "file",
      "pattern": "**/*",
      "content_rules": [
        {
          "mode": "regex",
          "start": 0,
          "pattern": "(?i)mongo(?:db)?",
          "patterns": []
        }
      ]
    }
  ],
  "match_mode": "all"
}
```

### Context

**Problem:** Considering MongoDB for analytics events due to perceived better write throughput for time-series data and high cardinality event logs.

**Decision:** MongoDB is strictly prohibited from being integrated into the current technology stack, including for analytics events.

**Rationale:** There is an active and non-negotiable architectural constraint against MongoDB in the stack due to the critical requirement for ACID compliance across all billing and user data. MongoDB does not satisfy this fundamental requirement.

**Alternatives Considered:**
- **MongoDB for analytics events**: It violates an active architectural constraint due to its lack of native ACID compliance, which is non-negotiable for billing and user data within our stack.

---

<!-- DECISION-DEC-260C41 -->
## Decision: Use shared secret token authentication for reporting worker communication

**Status**: Active  
**Date**: 2026-05-08  
**Severity**: Critical

**Files**:
- `src/reporting-worker/api-client.ts`
- `src/api/auth/middleware.ts`

**Rules**:
```json
{
  "conditions": [
    {
      "type": "file",
      "pattern": "src/reporting-worker/api-client.ts",
      "content_rules": [
        {
          "mode": "string",
          "patterns": [
            "https",
            "tls",
            "clientCert"
          ]
        }
      ]
    },
    {
      "type": "file",
      "pattern": "src/api/auth/middleware.ts",
      "content_rules": [
        {
          "mode": "regex",
          "start": 1,
          "pattern": "mtls|certificate"
        }
      ]
    }
  ],
  "match_mode": "any"
}
```

### Context

**Problem:** The reporting worker requires authentication to communicate with the main API, but mTLS setup is perceived as too slow or complex for this specific integration.

**Decision:** Bypass mTLS authentication for the new reporting worker and implement a hardcoded shared secret token in the HTTP header for inter-service authentication.

**Rationale:** The team chose a shared secret token approach to prioritize communication speed and reduce the implementation overhead compared to the mTLS setup.

**Alternatives Considered:**
- **mTLS**: The team felt it would be too slow and complex to implement for this specific worker.

---

<!-- DECISION-RAT-02E6AC -->
## Decision: Enforce 5-minute token expiry for authentication service

**Status**: Active  
**Date**: 2026-05-08  
**Severity**: Critical

**Files**:
- `services/auth-service`

**Rules**:
```json
{
  "conditions": [
    {
      "type": "file",
      "pattern": "services/auth-service/**/*",
      "content_rules": [
        {
          "mode": "regex",
          "start": 1,
          "pattern": "(expiresIn|expiry|ttl|lifetime).*?([6-9]\\d{2,}|[1-9]\\d{3,}|[0-9]{4,})"
        }
      ]
    }
  ],
  "match_mode": "all"
}
```

### Context

**Problem:** Determine the optimal token expiration duration for the authentication service to balance security and usability.

**Decision:** Implement a strict 5-minute token expiry window for the authentication service.

**Rationale:** This decision is driven by compliance requirements mandating rapid session invalidation and the need to mitigate the risk of replay attacks associated with longer-lived tokens.

---

<!-- DECISION-DEC-E90978 -->
## Decision: Migrate email service to Zoho and update SMTP infrastructure

**Status**: Active  
**Date**: 2026-04-28  
**Severity**: Critical

**Files**:
- `infrastructure/mail`
- `services/smtp`
- `config/email_routing`

**Rules**:
```json
{
  "conditions": [
    {
      "type": "file",
      "pattern": "{infrastructure/mail/**,services/smtp/**,config/email_routing/**}",
      "content_rules": [
        {
          "mode": "regex",
          "pattern": "(legacy_smtp|smtp_old|old_mail_server)",
          "patterns": [
            "legacy_smtp",
            "smtp_old"
          ]
        }
      ]
    }
  ],
  "match_mode": "any"
}
```

### Context

**Problem:** Need to replace the existing webmaster mailing service and transition away from the current SMTP server.

**Decision:** Migrate all email services to Zoho and update the SMTP server infrastructure, including the implementation of new routing rules to block any traffic to the legacy SMTP server.

**Rationale:** The team decided to move to Zoho to consolidate mailing services and address the limitations or overhead associated with the existing legacy SMTP infrastructure.

---

<!-- DECISION-DEC-BD1748 -->
## Decision: Migrate from Shipsy to in-house mapping event system

**Status**: Active  
**Date**: 2026-04-26  
**Severity**: Critical

**Files**:
- `services/shipping-integration`
- `infrastructure/event-bus`

### Context

**Problem:** Dependency on third-party provider Shipsy is causing scalability issues.

**Decision:** Switch from the third-party Shipsy provider to an in-house developed mapping event system.

**Rationale:** The team identified that the Shipsy service was negatively impacting the platform's scalability, and moving to an internal solution reduces external dependencies.

**Alternatives Considered:**
- **Continue using Shipsy**: It acts as a bottleneck for system scalability.

---

<!-- DECISION-DEC-D01A8D -->
## Decision: Abandon RFC 7807 for error responses

**Status**: Active  
**Date**: 2026-04-22  
**Severity**: Critical

**Files**:
- `api/responses`
- `api/error-handling`

**Rules**:
```json
{
  "conditions": [
    {
      "type": "file",
      "pattern": "api/responses/**/*",
      "content_rules": [
        {
          "mode": "string",
          "patterns": [
            "application/problem+json",
            "type",
            "title",
            "status",
            "detail",
            "instance"
          ]
        }
      ]
    },
    {
      "type": "file",
      "pattern": "api/error-handling/**/*",
      "content_rules": [
        {
          "mode": "string",
          "patterns": [
            "application/problem+json",
            "type",
            "title",
            "status",
            "detail",
            "instance"
          ]
        }
      ]
    }
  ],
  "match_mode": "any"
}
```

### Context

**Problem:** The current API error handling standard (RFC 7807) is considered outdated for the team's needs.

**Decision:** We have decided to officially discontinue the use of RFC 7807 (Problem Details for HTTP APIs) for all API error responses moving forward.

**Rationale:** The team determined that the RFC 7807 specification is outdated and no longer aligns with the current requirements and standards of the API architecture.

---

<!-- DECISION-OWN-08631B -->
## Decision: Use long-running containers for billing service instead of serverless functions

**Status**: Active  
**Date**: 2026-04-22  
**Severity**: Critical

**Files**:
- `packages/api/src/routes/credits.ts`
- `packages/decision-store/src/repositories/credit-repository.ts`
- `packages/common/src/types/credits.ts`
- `services/billing`

**Rules**:
```json
{
  "conditions": [
    {
      "type": "file",
      "pattern": "packages/{api/src/routes/credits.ts,decision-store/src/repositories/credit-repository.ts,common/src/types/credits.ts}",
      "content_rules": [
        {
          "mode": "regex",
          "start": 0,
          "pattern": "number|double"
        }
      ],
      "content_match_mode": "any"
    }
  ],
  "match_mode": "all"
}
```

### Context

**Problem:** Uncertainty regarding ownership of the billing module and the requirements for implementing new effort modes.

**Decision:** The billing service uses long-running containers instead of serverless functions.

**Rationale:** Serverless functions introduced cold starts which resulted in unacceptable latency spikes during traffic peaks, negatively impacting the user experience for the billing service.

**Alternatives Considered:**
- **Serverless functions**: Caused unacceptable latency spikes due to cold starts during traffic peaks.

---

<!-- DECISION-DEC-AC87F0 -->
## Decision: Define Model Fallback Ordering Strategy for API Rate Limits

**Status**: Active  
**Date**: 2026-04-22  
**Severity**: Critical

**Files**:
- `src/llm/client_factory.py`
- `src/llm/fallback_logic.py`

**Rules**:
```json
{
  "conditions": [
    {
      "type": "file",
      "pattern": "src/llm/{client_factory,fallback_logic}.py",
      "content_rules": [
        {
          "mode": "regex",
          "start": 0,
          "pattern": "(?i)fallback"
        }
      ],
      "content_match_mode": "all"
    }
  ],
  "match_mode": "all"
}
```

### Context

**Problem:** Handling 429 rate limit errors from LLM providers during extraction and detection tasks.

**Decision:** Establish explicit provider fallback orderings: For extraction, use Anthropic → DeepSeek → OpenAI. For detection, use Google → OpenAI → DeepSeek.

**Rationale:** To maintain system reliability and avoid task failure when individual LLM providers hit rate limits, a hierarchical fallback mechanism ensures work is diverted to alternative models before resorting to the Dead Letter Queue (DLQ) after retries.

---

<!-- DECISION-DEC-ADD28E -->
## Decision: Migrate infrastructure orchestration from AWS ECS to AWS EKS

**Status**: Active  
**Date**: 2026-04-22  
**Severity**: Critical

**Files**:
- `infrastructure/terraform`
- `infrastructure/k8s`

**Rules**:
```json
{
  "conditions": [
    {
      "type": "file",
      "pattern": "infrastructure/terraform/**/*",
      "content_rules": [
        {
          "mode": "string",
          "patterns": [
            "aws_ecs_cluster",
            "aws_ecs_service",
            "aws_ecs_task_definition"
          ]
        }
      ],
      "content_match_mode": "any"
    }
  ],
  "match_mode": "any"
}
```

### Context

**Problem:** AWS ECS lacks built-in support for multi-region failover without complex custom routing and requires additional tooling for advanced scaling capabilities.

**Decision:** The team will migrate from AWS ECS to AWS EKS for container orchestration.

**Rationale:** EKS provides superior orchestration flexibility, including native Horizontal Pod Autoscaler and improved multi-AZ/multi-region failover capabilities, which are necessary for the current scale, outweighing the operational overhead of Kubernetes.

**Alternatives Considered:**
- **AWS ECS**: Lacks sufficient multi-region failover support and requires custom routing implementations.
- **Railway**: Retained only as a temporary fallback, deemed insufficient for long-term production orchestration.

---

<!-- DECISION-DEC-811451 -->
## Decision: Standardize on PostgreSQL with pgvector for primary storage and vector search

**Status**: Active  
**Date**: 2026-04-22  
**Severity**: Critical

**Rules**:
```json
{
  "conditions": [
    {
      "type": "file",
      "pattern": "**/*",
      "content_rules": [
        {
          "mode": "regex",
          "start": 1,
          "pattern": "(?i)(mongodb|cockroachdb|elasticsearch|pinecone)",
          "patterns": [
            "mongodb",
            "cockroachdb",
            "elasticsearch",
            "pinecone"
          ]
        }
      ]
    }
  ],
  "match_mode": "all"
}
```

### Context

**Problem:** Selecting the primary datastore to handle both standard relational data and vector search requirements efficiently.

**Decision:** Use PostgreSQL with pgvector and HNSW indexes as the standard solution for primary datastore and vector search operations.

**Rationale:** PostgreSQL with pgvector provides the ability to manage both SQL-based relational data and vector search capabilities within a single system, simplifying the architecture compared to managing separate databases.

**Alternatives Considered:**
- **MongoDB**: The team preferred the relational capabilities of PostgreSQL and the unified support for vector search provided by pgvector.
- **CockroachDB**: The team decided that PostgreSQL with pgvector was sufficient and preferred over the complexity or features offered by CockroachDB.

---

<!-- DECISION-DEC-3213C5 -->
## Decision: Use MongoDB Atlas for schemaless analytics webhook storage

**Status**: Active  
**Date**: 2026-04-19  
**Severity**: Critical

**Files**:
- `services/analytics-webhook-handler`
- `infrastructure/database-clusters`

**Rules**:
```json
{
  "conditions": [
    {
      "type": "file",
      "pattern": "services/analytics-webhook-handler/**",
      "content_rules": [
        {
          "mode": "string",
          "patterns": [
            "INSERT INTO",
            "UPDATE ",
            "pg_query"
          ]
        }
      ]
    },
    {
      "type": "file",
      "pattern": "infrastructure/database-clusters/**",
      "content_rules": [
        {
          "mode": "regex",
          "start": 0,
          "pattern": "postgresql"
        }
      ]
    }
  ],
  "match_mode": "any"
}
```

### Context

**Decision:** Use MongoDB Atlas specifically for the analytics event ingestion pipeline, while keeping all other core application data in PostgreSQL.

**Rationale:** MongoDB Atlas provides the necessary horizontal sharding and schemaless structure to handle the required 50k write operations per second, whereas PostgreSQL performance degrades under this load.

**Alternatives Considered:**
- **PostgreSQL JSONB**: Proved too difficult and inefficient to index effectively for schemaless event data.

---

<!-- DECISION-DEC-465C1E -->
## Decision: Use MongoDB for Analytics Events Pipeline

**Status**: Active  
**Date**: 2026-04-18  
**Severity**: Critical

**Files**:
- `packages/api/src/analytics/`

**Rules**:
```json
{
  "conditions": [
    {
      "type": "file",
      "exclude": [
        "packages/api/src/analytics/**/*.test.{ts,js,go,py}",
        "packages/api/src/analytics/migrations/**/*"
      ],
      "pattern": "packages/api/src/analytics/**/*.{ts,js,go,py}",
      "content_rules": [
        {
          "mode": "string",
          "patterns": [
            "pg",
            "postgres",
            "postgresql",
            "new Client(",
            "createPool(",
            "sequelize",
            "typeorm",
            "knex"
          ]
        }
      ],
      "content_match_mode": "any"
    }
  ],
  "match_mode": "any"
}
```

### Context

**Problem:** PostgreSQL's write throughput is insufficient for high-cardinality analytics event data, failing to meet new scale requirements.

**Decision:** We will use MongoDB for the analytics events pipeline, provisioning a MongoDB Atlas cluster to handle the data.

**Rationale:** MongoDB offers 10x the write throughput compared to PostgreSQL for high-cardinality event data, which is essential to meet the current scale requirements. The previous constraint was established before these new scale demands emerged.

**Alternatives Considered:**
- **PostgreSQL**: PostgreSQL's write throughput is 10x lower than MongoDB for high-cardinality event data, making it unsuitable for the new scale requirements of the analytics pipeline.

---

<!-- DECISION-DEC-3B0624 -->
## Decision: LLM Provider Strategy by Pipeline Step and Effort Mode

**Status**: Active  
**Date**: 2026-04-18  
**Severity**: Critical

**Files**:
- `packages/analyzer/`

### Context

**Decision:** Each LLM pipeline step (detection, extraction, formatting) has its own provider configuration managed via environment variables. An 'effort mode' concept allows overriding these configurations per company at request time, defining specific LLM models for different quality/cost tiers: Saver uses gemini-flash, Balanced mixes gemini-flash, claude-haiku, and gpt-4o-mini, Pro uses claude-sonnet for extraction, and Super uses claude-opus.

**Rationale:** The strategy is designed to provide flexibility and optimization across different pipeline steps and 'effort modes'. By configuring providers per step and allowing overrides based on company effort modes, the system can balance cost, performance, and model quality according to specific requirements, from 'Saver' (likely cost-optimized) to 'Super' (likely highest quality/cost). The multi-provider abstraction facilitates this dynamic selection.

---

<!-- DECISION-PLAN-71727A -->
## Decision: Plan to Migrate Application Infrastructure from Railway to AWS ECS

**Status**: Active  
**Date**: 2026-04-18  
**Severity**: Critical

**Rules**:
```json
{
  "conditions": [
    {
      "type": "file",
      "pattern": "**/*",
      "content_rules": [
        {
          "mode": "regex",
          "start": 0,
          "pattern": "(?i)(migrate|aws|ecs|railway)"
        }
      ]
    }
  ],
  "match_mode": "any"
}
```

### Context

**Problem:** The current hosting platform, Railway, becomes cost-prohibitive at scale (exceeding $500/month) and lacks the VPC isolation capabilities required for enterprise customers.

**Decision:** The trigger metric for initiating the AWS migration has been adjusted from 20 paying customers to 30 paying customers. The Q3 2026 timeline for the migration still holds.

**Rationale:** This adjustment is due to Railway costs being more predictable than initially expected. Additionally, the VPC isolation requirement, which was a significant factor, only applies to enterprise customers, a segment we are targeting at a later stage.

---

<!-- DECISION-DEC-670907 -->
## Decision: Defer Microservices Adoption, Maintain Monorepo Architecture

**Status**: Active  
**Date**: 2026-04-18  
**Severity**: Critical

**Rules**:
```json
{
  "conditions": [
    {
      "type": "file",
      "pattern": "**/*",
      "content_rules": [
        {
          "mode": "regex",
          "start": 1,
          "pattern": "(?i)grpc|microservice|service-mesh|distributed-tracing"
        }
      ]
    }
  ],
  "match_mode": "all"
}
```

### Context

**Problem:** The team considered adopting a microservices architecture for the recorder and analyzer components but faced challenges.

**Decision:** We will integrate decision-guardian into our PR pipeline to enforce and track architectural decisions.

**Rationale:** Automating the verification of architectural decisions during the review process helps maintain consistency and ensures that developers adhere to established guidelines.

**Alternatives Considered:**
- **Adopt a microservices architecture by splitting recorder and analyzer into separate gRPC services.**: The previous attempt in Phase 1 led to brutal deployment complexity for a 3-person team, consuming 40% of their time debugging inter-service authentication and network failures.

---

<!-- DECISION-CONV-127DF1 -->
## Decision: Standardize API Error Responses to RFC 7807

**Status**: Active  
**Date**: 2026-04-18  
**Severity**: Critical

**Files**:
- `auth endpoint`
- `cursorrules`
- `All API error handling implementations`

### Context

**Problem:** Inconsistent API error response formats across different endpoints, with some returning raw message strings.

**Decision:** All API errors must adhere to the RFC 7807 problem details format, including fields such as type, title, status, detail, and instance.

**Rationale:** To ensure consistency across all API endpoints and prevent the generation of non-compliant errors, especially from AI-assisted code.

---

<!-- DECISION-DEC-B6869B -->
## Decision: Define LLM Model Combinations for Saver, Balanced, Pro, and Super Effort Modes

**Status**: Active  
**Date**: 2026-04-18  
**Severity**: Critical

**Files**:
- `**/*`

### Context

**Problem:** We need to lock down exactly which model combination maps to which effort mode.

**Decision:** The specific LLM model combinations for the multi-provider effort modes were finalized: Saver mode uses `gemini-flash` for detection, extraction, and format. Balanced mode uses `gemini-flash` for detection, `claude-haiku` for extraction, and `gpt-4o-mini` for format. Pro mode uses `gemini-flash` for detection, `claude-sonnet` for extraction, and `gpt-4o-mini` for format. Super mode uses `gemini-flash` for detection, `claude-opus` for extraction, and `claude-sonnet` for format.

**Rationale:** The chosen LLM model combinations for each effort mode (Saver, Balanced, Pro, Super) were selected to provide different performance and cost profiles, aligning with the multi-provider strategy. Cost analysis confirmed that the proposed combinations, ranging from ~$0.08/1M tokens for Saver to ~$4.50/1M tokens for Super, ensure fine margins at current credit pricing.

---

<!-- DECISION-DEC-6ACD06 -->
## Decision: Implement Multi-Provider LLM Abstraction for Pipeline Steps with Per-Company Overrides

**Status**: Active  
**Date**: 2026-04-18  
**Severity**: Critical

**Rules**:
```json
{
  "conditions": [
    {
      "type": "file",
      "exclude": [
        "**/*test*",
        "**/*doc*"
      ],
      "pattern": "**/*.(ts|js|py|go|java|cs|yml|yaml|env|ini|properties|json)",
      "content_rules": [
        {
          "mode": "regex",
          "start": 0,
          "pattern": "(new|import|from)[^;\\n]*?(Anthropic|OpenAI|GoogleCloud|AzureOpenAI|Claude|Gemini|GPT-4o)",
          "patterns": []
        },
        {
          "mode": "regex",
          "start": 0,
          "pattern": "(LLM_PROVIDER|LLM_MODEL)[^=\\n]*?=(?!.*(config|abstraction|env))[^\\n]*(Claude-Sonnet|Gemini-Flash|GPT-4o-mini)",
          "patterns": []
        }
      ],
      "content_match_mode": "any"
    }
  ],
  "match_mode": "any"
}
```

### Context

**Problem:** The current LLM provider strategy is unmaintainable, using different providers (Gemini-Flash, Claude-Sonnet, GPT-4o-mini) for different pipeline steps, leading to high costs (Claude-Sonnet is 60% of the bill) and inconsistent availability (Sonnet outages).

**Decision:** We will implement a multi-provider abstraction where each pipeline step (detection, extraction, enrichment, formatting) has its own LLM provider configuration via environment variables. At request time, an 'effort mode' can override the provider selection on a per-company basis.

**Rationale:** This approach allows companies with high context volume (Tier 3+) to pay extra for Claude-Sonnet's accuracy where needed, while companies with tighter budgets can use more cost-effective options like Gemini-Flash for all steps. It also decouples our infrastructure from individual LLM vendor stability and enables independent contract negotiations with different providers (Anthropic, OpenAI, Google).

**Alternatives Considered:**
- **Continue with current fragmented multi-provider setup (Gemini-Flash for detection, Claude-Sonnet for extraction, GPT-4o-mini for formatting).**: This approach is unmaintainable, costly (Claude-Sonnet accounts for 60% of the LLM bill), and suffers from inconsistent provider availability issues.
- **Consolidate to a single LLM provider for all pipeline steps.**: This would limit flexibility, potentially sacrificing accuracy for high-tier companies or forcing budget-conscious companies to pay for more expensive models than necessary. It would also lead to vendor lock-in and a single point of failure for LLM stability.

---

<!-- DECISION-DEC-A0711B -->
## Decision: Direct database access in Server Components for order history

**Status**: Active  
**Date**: 2026-05-08  
**Severity**: Warning

**Files**:
- `src/components/user/Profile.server.ts`

**Rules**:
```json
{
  "conditions": [
    {
      "type": "file",
      "pattern": "src/components/user/Profile.server.ts",
      "content_rules": [
        {
          "mode": "regex",
          "start": 0,
          "pattern": "new\\s+GraphQLResolver"
        }
      ]
    }
  ],
  "match_mode": "all"
}
```

### Context

**Problem:** Avoid writing and reviewing new GraphQL resolvers for fetching user order history in Server Components.

**Decision:** Implement direct PostgreSQL connection within the User Profile Server Component to fetch order history.

**Rationale:** Direct database access reduces development effort by bypassing the overhead of creating and maintaining additional GraphQL resolvers for simple data retrieval tasks.


<!-- decispher: output truncated to context budget -->
