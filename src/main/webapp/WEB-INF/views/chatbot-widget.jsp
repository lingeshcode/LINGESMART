<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!-- AI Chatbot Drawer Floating Widget (Phase 3 Spec) -->
<style>
    .chat-widget-btn {
        position: fixed;
        bottom: 24px;
        right: 24px;
        width: 60px;
        height: 60px;
        border-radius: 50%;
        background: linear-gradient(135deg, #2874f0, #0049b7);
        color: white;
        border: none;
        box-shadow: 0 4px 12px rgba(0,0,0,0.25);
        cursor: pointer;
        z-index: 1000;
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 1.5rem;
        transition: transform 0.2s;
    }
    .chat-widget-btn:hover {
        transform: scale(1.08);
    }
    .chat-panel {
        position: fixed;
        bottom: 95px;
        right: 24px;
        width: 360px;
        height: 480px;
        background: #ffffff;
        border-radius: 12px;
        box-shadow: 0 8px 24px rgba(0,0,0,0.15);
        z-index: 1000;
        display: none;
        flex-direction: column;
        overflow: hidden;
        border: 1px solid #e0e0e0;
    }
    .chat-header {
        background: #2874f0;
        color: white;
        padding: 14px 16px;
        font-weight: 700;
        display: flex;
        justify-content: space-between;
        align-items: center;
    }
    .chat-body {
        flex: 1;
        padding: 12px;
        overflow-y: auto;
        background: #f9f9f9;
        display: flex;
        flex-direction: column;
        gap: 8px;
    }
    .chat-msg {
        max-width: 80%;
        padding: 8px 12px;
        border-radius: 12px;
        font-size: 0.88rem;
        line-height: 1.4;
    }
    .chat-msg.user {
        align-self: flex-end;
        background: #2874f0;
        color: white;
        border-bottom-right-radius: 2px;
    }
    .chat-msg.bot {
        align-self: flex-start;
        background: #e9ecef;
        color: #212529;
        border-bottom-left-radius: 2px;
    }
    .chat-footer {
        padding: 10px;
        border-top: 1px solid #eee;
        background: white;
        display: flex;
        gap: 8px;
    }
    .chat-footer input {
        border-radius: 20px;
    }
</style>

<button class="chat-widget-btn" id="chatToggleBtn" title="Ask LingeshMart AI Assistant">
    <i class="fa-solid fa-robot"></i>
</button>

<div class="chat-panel" id="chatPanel">
    <div class="chat-header">
        <div><i class="fa-solid fa-sparkles me-2"></i>LingeshMart AI Assistant</div>
        <button class="btn btn-sm text-white p-0" id="chatCloseBtn"><i class="fa-solid fa-xmark fs-5"></i></button>
    </div>
    <div class="chat-body" id="chatBody">
        <div class="chat-msg bot">
            Hello! I am your LingeshMart AI assistant. Ask me anything about products, delivery, shipping, or returns!
        </div>
    </div>
    <div class="chat-footer">
        <input type="text" class="form-control form-control-sm px-3" id="chatInput" placeholder="Ask a question..." onkeydown="if(event.key==='Enter') sendChatMessage()">
        <button class="btn btn-primary btn-sm rounded-circle px-3" onclick="sendChatMessage()"><i class="fa-solid fa-paper-plane"></i></button>
    </div>
</div>

<script>
    const chatBtn = document.getElementById('chatToggleBtn');
    const chatPanel = document.getElementById('chatPanel');
    const chatCloseBtn = document.getElementById('chatCloseBtn');
    const chatBody = document.getElementById('chatBody');
    const chatInput = document.getElementById('chatInput');

    chatBtn.addEventListener('click', () => {
        chatPanel.style.display = (chatPanel.style.display === 'flex') ? 'none' : 'flex';
    });

    chatCloseBtn.addEventListener('click', () => {
        chatPanel.style.display = 'none';
    });

    async function sendChatMessage() {
        const text = chatInput.value.trim();
        if (!text) return;

        // Render user message
        const userMsg = document.createElement('div');
        userMsg.className = 'chat-msg user';
        userMsg.textContent = text;
        chatBody.appendChild(userMsg);
        chatInput.value = '';
        chatBody.scrollTop = chatBody.scrollHeight;

        // Render loading bot indicator
        const botMsg = document.createElement('div');
        botMsg.className = 'chat-msg bot';
        botMsg.textContent = 'Thinking...';
        chatBody.appendChild(botMsg);
        chatBody.scrollTop = chatBody.scrollHeight;

        try {
            const response = await fetch('${pageContext.request.contextPath}/api/v1/chat', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ message: text })
            });
            const data = await response.json();
            if (data.success && data.data) {
                botMsg.textContent = data.data.reply;
            } else if (data.error) {
                botMsg.textContent = data.error.message;
            } else {
                botMsg.textContent = 'Sorry, I could not process your request right now.';
            }
        } catch (err) {
            botMsg.textContent = 'Connection error. Please try again.';
        }
        chatBody.scrollTop = chatBody.scrollHeight;
    }
</script>
