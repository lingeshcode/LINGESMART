const http = require('http');
const fs = require('fs');
const path = require('path');
const url = require('url');

const PORT = 8080;

// Seed Database
let users = [
    { id: 1, name: 'Lingesh Admin', email: 'admin@lingeshmart.com', role: 'ADMIN' },
    { id: 2, name: 'TechZone Electronics', email: 'seller1@lingeshmart.com', role: 'SELLER' },
    { id: 3, name: 'StyleHub Fashion', email: 'seller2@lingeshmart.com', role: 'SELLER' },
    { id: 4, name: 'John Buyer', email: 'buyer1@lingeshmart.com', role: 'BUYER' }
];

let products = [
    { id: 1, sellerId: 2, name: 'iPhone 15 Pro Max (256 GB)', price: 134900, stockQty: 15, category: 'Electronics', imageUrl: 'https://images.unsplash.com/photo-1695048133142-1a20484d2569?w=600', description: 'Titanium design, A17 Pro chip, 48MP camera.' },
    { id: 2, sellerId: 2, name: 'MacBook Air M3 (16GB, 512GB)', price: 114900, stockQty: 10, category: 'Electronics', imageUrl: 'https://images.unsplash.com/photo-1517336714731-489689fd1ca8?w=600', description: 'Liquid Retina display, supercharged by M3.' },
    { id: 3, sellerId: 2, name: 'Sony WH-1000XM5 Headphones', price: 29990, stockQty: 25, category: 'Electronics', imageUrl: 'https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=600', description: 'Industry leading noise canceling wireless headphones.' },
    { id: 4, sellerId: 3, name: 'Men Leather Biker Jacket', price: 4999, stockQty: 40, category: 'Fashion', imageUrl: 'https://images.unsplash.com/photo-1551028719-00167b16eac5?w=600', description: '100% Genuine lambskin leather jacket.' },
    { id: 5, sellerId: 3, name: 'Women Designer Floral Saree', price: 2499, stockQty: 50, category: 'Fashion', imageUrl: 'https://images.unsplash.com/photo-1610030469983-98e550d6193c?w=600', description: 'Silk blend handwoven floral saree.' },
    { id: 6, sellerId: 2, name: 'Samsung 55-inch Crystal 4K TV', price: 42990, stockQty: 8, category: 'Home', imageUrl: 'https://images.unsplash.com/photo-1593784991095-a205069470b6?w=600', description: 'PurColor, Crystal Processor 4K.' },
    { id: 7, sellerId: 3, name: 'Ergonomic Mesh Office Chair', price: 7999, stockQty: 30, category: 'Home', imageUrl: 'https://images.unsplash.com/photo-1580481072645-022f9a6d1270?w=600', description: 'Adjustable lumbar support for long working hours.' },
    { id: 8, sellerId: 2, name: 'Clean Code by Robert C. Martin', price: 899, stockQty: 100, category: 'Books', imageUrl: 'https://images.unsplash.com/photo-1544716278-ca5e3f4abd8c?w=600', description: 'A Handbook of Agile Software Craftsmanship.' }
];

let cart = [];
let orders = [
    { id: 101, buyerName: 'John Buyer', totalAmount: 29990, status: 'DELIVERED', createdAt: '2026-10-01' }
];
let reviews = [
    { id: 1, productId: 3, userName: 'John Buyer', rating: 5, comment: 'Unbelievable noise cancellation! Perfect for studying.' }
];

const htmlTemplate = (content, user = null) => `
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>LingeshMart - Premium Marketplace</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;500;600;700;800&display=swap" rel="stylesheet">
    <style>
        body { font-family: 'Plus Jakarta Sans', sans-serif; background: #f1f3f6; color: #212121; }
        .navbar-flipkart { background-color: #2874f0; box-shadow: 0 2px 4px rgba(0,0,0,.08); }
        .navbar-brand-title { font-weight: 800; font-style: italic; color: #fff; font-size: 1.5rem; }
        .navbar-brand-sub { font-size: 0.75rem; color: #ffe500; font-weight: 600; margin-top: -6px; }
        .btn-flipkart { background-color: #fb641b; color: #fff; font-weight: 700; border-radius: 4px; border: none; }
        .btn-flipkart:hover { background-color: #e65612; color: #fff; }
        .btn-cart { background-color: #ff9f00; color: #fff; font-weight: 700; border-radius: 4px; border: none; }
        .card-product { border: none; border-radius: 8px; transition: transform 0.2s; }
        .card-product:hover { transform: translateY(-4px); box-shadow: 0 8px 16px rgba(0,0,0,0.1); }
        .price-tag { font-size: 1.25rem; font-weight: 700; color: #212121; }
        .chat-widget-btn { position: fixed; bottom: 24px; right: 24px; width: 60px; height: 60px; border-radius: 50%; background: linear-gradient(135deg, #2874f0, #0049b7); color: white; border: none; box-shadow: 0 4px 12px rgba(0,0,0,0.25); cursor: pointer; z-index: 1000; display: flex; align-items: center; justify-content: center; font-size: 1.5rem; }
        .chat-panel { position: fixed; bottom: 95px; right: 24px; width: 350px; height: 450px; background: #fff; border-radius: 12px; box-shadow: 0 8px 24px rgba(0,0,0,0.15); z-index: 1000; display: none; flex-direction: column; overflow: hidden; border: 1px solid #e0e0e0; }
    </style>
</head>
<body>
<nav class="navbar navbar-expand-lg navbar-dark navbar-flipkart sticky-top py-2">
    <div class="container">
        <a class="navbar-brand d-flex flex-column me-4" href="/lingeshmart">
            <span class="navbar-brand-title"><i class="fa-solid fa-cart-shopping me-2"></i>LingeshMart</span>
            <span class="navbar-brand-sub">Explore <span style="color: #fff;">Plus</span></span>
        </a>
        <form class="d-flex mx-auto w-50" action="/lingeshmart" method="get">
            <input class="form-control rounded-start" type="search" name="q" placeholder="Search for products, brands and more...">
            <button class="btn btn-light rounded-end text-primary" type="submit"><i class="fa-solid fa-magnifying-glass"></i></button>
        </form>
        <div class="d-flex align-items-center gap-3">
            <a href="/lingeshmart/cart" class="btn btn-light text-primary fw-bold"><i class="fa-solid fa-cart-shopping me-1"></i> Cart (${cart.length})</a>
            <a href="/lingeshmart/seller" class="btn btn-warning text-dark fw-bold"><i class="fa-solid fa-store me-1"></i> Seller Hub</a>
            <a href="/lingeshmart/admin" class="btn btn-danger text-white fw-bold"><i class="fa-solid fa-user-shield me-1"></i> Admin</a>
        </div>
    </div>
</nav>

<div class="container my-4">
    ${content}
</div>

<footer class="bg-dark text-white py-4 mt-5">
    <div class="container text-center text-muted small">
        &copy; 2026 LingeshMart Multi-Seller Marketplace. Built for Anna University R2025 Project.
    </div>
</footer>

<button class="chat-widget-btn" onclick="toggleChat()"><i class="fa-solid fa-robot"></i></button>
<div class="chat-panel" id="chatPanel">
    <div class="bg-primary text-white p-3 font-weight-bold d-flex justify-content-between">
        <span><i class="fa-solid fa-robot me-2"></i>LingeshMart AI Assistant</span>
        <span style="cursor:pointer" onclick="toggleChat()">✕</span>
    </div>
    <div class="p-3 flex-grow-1 overflow-auto" id="chatMessages" style="background:#f9f9f9;">
        <div class="bg-light p-2 rounded mb-2 small">Hello! How can I assist you with LingeshMart shopping today?</div>
    </div>
    <div class="p-2 border-top bg-white d-flex gap-2">
        <input type="text" class="form-control form-control-sm" id="chatInput" placeholder="Ask a question..." onkeydown="if(event.key==='Enter') sendChat()">
        <button class="btn btn-primary btn-sm" onclick="sendChat()"><i class="fa-solid fa-paper-plane"></i></button>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/bootstrap.bundle.min.js"></script>
<script>
    function toggleChat() {
        const p = document.getElementById('chatPanel');
        p.style.display = (p.style.display === 'flex') ? 'none' : 'flex';
    }
    async function sendChat() {
        const input = document.getElementById('chatInput');
        const text = input.value.trim();
        if (!text) return;
        const box = document.getElementById('chatMessages');
        box.innerHTML += '<div class="bg-primary text-white p-2 rounded mb-2 small text-end">'+text+'</div>';
        input.value = '';
        box.scrollTop = box.scrollHeight;

        const res = await fetch('/api/v1/chat', { method:'POST', headers:{'Content-Type':'application/json'}, body: JSON.stringify({message: text}) });
        const data = await res.json();
        const reply = (data.success && data.data) ? data.data.reply : 'I can help with orders, shipping, and product search!';
        box.innerHTML += '<div class="bg-light p-2 rounded mb-2 small">'+reply+'</div>';
        box.scrollTop = box.scrollHeight;
    }
</script>
</body>
</html>
`;

const server = http.createServer((req, res) => {
    const parsedUrl = url.parse(req.url, true);
    const pathname = parsedUrl.pathname;
    const method = req.method;

    // API v1 Health Check
    if (pathname === '/api/v1/health') {
        res.writeHead(200, { 'Content-Type': 'application/json' });
        return res.end(JSON.stringify({ success: true, data: { status: 'UP', db: 'UP' } }));
    }

    // API v1 Chatbot proxy endpoint
    if (pathname === '/api/v1/chat' && method === 'POST') {
        let body = '';
        req.on('data', chunk => body += chunk);
        req.on('end', () => {
            let msg = '';
            try { msg = JSON.parse(body).message || ''; } catch(e){}
            let reply = "LingeshMart shipping takes 2-3 business days across India. All orders include 7-day returns.";
            if (msg.toLowerCase().includes('phone') || msg.toLowerCase().includes('iphone')) reply = "We have iPhone 15 Pro Max (256 GB) in stock for ₹1,34,900!";
            if (msg.toLowerCase().includes('seller')) reply = "To sell products, access the Seller Hub from the top navigation bar.";
            res.writeHead(200, { 'Content-Type': 'application/json' });
            res.end(JSON.stringify({ success: true, data: { reply } }));
        });
        return;
    }

    // Add to Cart Action
    if (pathname === '/lingeshmart/cart/add') {
        const query = parsedUrl.query;
        const prodId = parseInt(query.productId);
        const p = products.find(x => x.id === prodId);
        if (p) cart.push(p);
        res.writeHead(302, { 'Location': '/lingeshmart/cart' });
        return res.end();
    }

    // Clear Cart Action
    if (pathname === '/lingeshmart/cart/clear') {
        cart = [];
        res.writeHead(302, { 'Location': '/lingeshmart/cart' });
        return res.end();
    }

    // Checkout Order Placement Action
    if (pathname === '/lingeshmart/checkout/confirm') {
        let total = cart.reduce((acc, curr) => acc + curr.price, 0);
        if (total > 0) {
            orders.unshift({ id: 100 + orders.length + 1, buyerName: 'John Buyer', totalAmount: total, status: 'CONFIRMED', createdAt: new Date().toISOString().substring(0, 10) });
            cart = [];
        }
        res.writeHead(302, { 'Location': '/lingeshmart/orders' });
        return res.end();
    }

    // Cart View
    if (pathname === '/lingeshmart/cart') {
        let total = cart.reduce((acc, curr) => acc + curr.price, 0);
        let itemsHtml = cart.length === 0 ? '<div class="alert alert-info py-4 text-center">Your shopping cart is empty!</div>' :
            cart.map(item => `
                <div class="d-flex justify-content-between align-items-center p-3 border-bottom bg-white rounded mb-2">
                    <div class="d-flex align-items-center gap-3">
                        <img src="${item.imageUrl}" style="width:60px;height:60px;object-fit:contain;">
                        <div>
                            <h6 class="mb-0 fw-bold">${item.name}</h6>
                            <span class="text-muted small">${item.category}</span>
                        </div>
                    </div>
                    <div class="fw-bold fs-5">₹${item.price.toLocaleString('en-IN')}</div>
                </div>
            `).join('');

        let content = `
            <h3 class="fw-bold mb-4"><i class="fa-solid fa-cart-shopping me-2 text-primary"></i>Shopping Cart</h3>
            <div class="row g-4">
                <div class="col-md-8">${itemsHtml}</div>
                <div class="col-md-4">
                    <div class="card border-0 shadow-sm p-4">
                        <h5 class="fw-bold border-bottom pb-2">Price Details</h5>
                        <div class="d-flex justify-content-between my-2"><span>Items (${cart.length})</span><span>₹${total.toLocaleString('en-IN')}</span></div>
                        <div class="d-flex justify-content-between my-2"><span>Delivery</span><span class="text-success fw-bold">FREE</span></div>
                        <hr>
                        <div class="d-flex justify-content-between fs-5 fw-bold mb-4"><span>Total Amount</span><span class="text-primary">₹${total.toLocaleString('en-IN')}</span></div>
                        ${cart.length > 0 ? '<a href="/lingeshmart/checkout" class="btn btn-flipkart btn-lg w-100 py-3">Proceed to Checkout</a>' : ''}
                    </div>
                </div>
            </div>
        `;
        res.writeHead(200, { 'Content-Type': 'text/html' });
        return res.end(htmlTemplate(content));
    }

    // Checkout View
    if (pathname === '/lingeshmart/checkout') {
        let total = cart.reduce((acc, curr) => acc + curr.price, 0);
        let content = `
            <div class="row justify-content-center">
                <div class="col-md-7">
                    <div class="card border-0 shadow-sm p-4">
                        <h3 class="fw-bold text-primary mb-3"><i class="fa-solid fa-credit-card me-2"></i>Checkout & Payment Confirmation</h3>
                        <p class="text-muted small">Mock Payment Step (Specification Rule F5)</p>
                        <div class="bg-light p-3 rounded mb-4">
                            <div class="d-flex justify-content-between fs-5 fw-bold"><span>Total Amount</span><span class="text-success">₹${total.toLocaleString('en-IN')}</span></div>
                        </div>
                        <a href="/lingeshmart/checkout/confirm" class="btn btn-flipkart btn-lg w-100 py-3 font-weight-bold">Confirm Order & Pay ₹${total.toLocaleString('en-IN')}</a>
                    </div>
                </div>
            </div>
        `;
        res.writeHead(200, { 'Content-Type': 'text/html' });
        return res.end(htmlTemplate(content));
    }

    // Orders History View
    if (pathname === '/lingeshmart/orders') {
        let listHtml = orders.map(o => `
            <div class="card border-0 shadow-sm mb-3">
                <div class="card-header bg-light d-flex justify-content-between py-3">
                    <div><strong>Order #LM-${o.id}</strong> • Placed on ${o.createdAt}</div>
                    <span class="badge bg-success px-3 py-2">${o.status}</span>
                </div>
                <div class="card-body d-flex justify-content-between align-items-center">
                    <div>Buyer: <strong>${o.buyerName}</strong></div>
                    <div class="fs-5 fw-bold text-primary">₹${o.totalAmount.toLocaleString('en-IN')}</div>
                </div>
            </div>
        `).join('');

        let content = `
            <h3 class="fw-bold mb-4"><i class="fa-solid fa-box-open me-2 text-primary"></i>Order History</h3>
            ${listHtml}
        `;
        res.writeHead(200, { 'Content-Type': 'text/html' });
        return res.end(htmlTemplate(content));
    }

    // Seller Dashboard View
    if (pathname === '/lingeshmart/seller') {
        let content = `
            <h3 class="fw-bold mb-4"><i class="fa-solid fa-store me-2 text-primary"></i>Seller Hub & Sales Dashboard</h3>
            <div class="row g-3 mb-4">
                <div class="col-md-4"><div class="card bg-primary text-white p-3 rounded-3"><h6 class="small">Active Products</h6><h3>${products.length}</h3></div></div>
                <div class="col-md-4"><div class="card bg-success text-white p-3 rounded-3"><h6 class="small">Total Sales Revenue</h6><h3>₹2,45,000</h3></div></div>
                <div class="col-md-4"><div class="card bg-info text-white p-3 rounded-3"><h6 class="small">Incoming Orders</h6><h3>${orders.length}</h3></div></div>
            </div>
            <div class="card border-0 shadow-sm">
                <div class="card-header bg-white py-3 fw-bold">Listed Products</div>
                <div class="card-body p-0">
                    <table class="table table-hover align-middle mb-0">
                        <thead><tr><th>Product</th><th>Category</th><th>Price</th><th>Stock</th></tr></thead>
                        <tbody>
                            ${products.map(p => `
                                <tr>
                                    <td><strong>${p.name}</strong></td>
                                    <td><span class="badge bg-secondary">${p.category}</span></td>
                                    <td class="fw-bold">₹${p.price.toLocaleString('en-IN')}</td>
                                    <td><span class="badge bg-success">${p.stockQty} in stock</span></td>
                                </tr>
                            `).join('')}
                        </tbody>
                    </table>
                </div>
            </div>
        `;
        res.writeHead(200, { 'Content-Type': 'text/html' });
        return res.end(htmlTemplate(content));
    }

    // Admin Dashboard View
    if (pathname === '/lingeshmart/admin') {
        let content = `
            <h3 class="fw-bold mb-4"><i class="fa-solid fa-user-shield me-2 text-warning"></i>Admin Control Panel</h3>
            <div class="card border-0 shadow-sm mb-4">
                <div class="card-header bg-white py-3 fw-bold">Listing Moderation & Users Audit</div>
                <div class="card-body p-0">
                    <table class="table table-hover align-middle mb-0">
                        <thead><tr><th>User / Seller</th><th>Email</th><th>Role</th></tr></thead>
                        <tbody>
                            ${users.map(u => `
                                <tr>
                                    <td><strong>${u.name}</strong></td>
                                    <td>${u.email}</td>
                                    <td><span class="badge bg-danger">${u.role}</span></td>
                                </tr>
                            `).join('')}
                        </tbody>
                    </table>
                </div>
            </div>
        `;
        res.writeHead(200, { 'Content-Type': 'text/html' });
        return res.end(htmlTemplate(content));
    }

    // Home / Catalog Browse View (Default)
    let q = parsedUrl.query.q || '';
    let category = parsedUrl.query.category || 'ALL';
    let filtered = products.filter(p => {
        let matchQ = !q || p.name.toLowerCase().includes(q.toLowerCase());
        let matchCat = category === 'ALL' || p.category.toLowerCase() === category.toLowerCase();
        return matchQ && matchCat;
    });

    let gridHtml = filtered.map(p => `
        <div class="col">
            <div class="card h-100 card-product shadow-sm">
                <div class="text-center p-3 bg-white" style="height: 200px;">
                    <img src="${p.imageUrl}" class="img-fluid h-100" style="object-fit: contain;">
                </div>
                <div class="card-body d-flex flex-column justify-content-between">
                    <div>
                        <span class="badge bg-light text-dark mb-1">${p.category}</span>
                        <h6 class="fw-bold text-dark text-truncate mb-2">${p.name}</h6>
                    </div>
                    <div>
                        <div class="price-tag mb-3">₹${p.price.toLocaleString('en-IN')}</div>
                        <a href="/lingeshmart/cart/add?productId=${p.id}" class="btn btn-cart btn-sm w-100 font-weight-bold"><i class="fa-solid fa-cart-shopping me-1"></i> Add to Cart</a>
                    </div>
                </div>
            </div>
        </div>
    `).join('');

    let content = `
        <div class="bg-primary text-white p-4 rounded-3 mb-4 shadow-sm">
            <h2 class="fw-bold mb-1">Welcome to LingeshMart Big Billion Days!</h2>
            <p class="mb-0 text-white-50">Unbeatable prices on Electronics, Fashion, Home Decor & Books.</p>
        </div>
        <div class="d-flex justify-content-between align-items-center mb-3">
            <h4 class="fw-bold mb-0">Featured Products</h4>
            <span class="text-muted small">${filtered.length} products found</span>
        </div>
        <div class="row row-cols-1 row-cols-sm-2 row-cols-md-4 g-4">${gridHtml}</div>
    `;

    res.writeHead(200, { 'Content-Type': 'text/html' });
    res.end(htmlTemplate(content));
});

server.listen(PORT, () => {
    console.log(`===================================================`);
    console.log(`  LingeshMart is LIVE at: http://localhost:${PORT}/lingeshmart`);
    console.log(`===================================================`);
});
