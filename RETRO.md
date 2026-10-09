# Sprint Retrospectives - LingeshMart

## Sprint 1 (Authentication & Base DAO)
- **What worked**: Clean layered MVC architecture and HikariCP connection pooling setup.
- **What didn't**: Initial schema setup required explicit H2 compatibility mode flags.
- **Next Sprint Action Item**: Implement transactional multi-item order placement.

## Sprint 2 (Core Order Journey & Mock Checkout)
- **What worked**: JDBC transactions with `setAutoCommit(false)` guaranteed stock consistency.
- **What didn't**: Session checks needed role-based path filtering.
- **Next Sprint Action Item**: Add seller analytics dashboard metrics.

## Sprint 3 (Phase 3 AI Chatbot & Security Checklist)
- **What worked**: AI Chatbot proxy servlet with graceful degradation on network timeouts.
- **What didn't**: Required strict session rate-limiting to prevent API quota exhaustion.
- **Next Sprint Action Item**: Complete regression testing and final documentation pass.
