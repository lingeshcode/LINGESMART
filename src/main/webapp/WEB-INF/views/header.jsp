<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>LingeshMart - Premium Marketplace</title>
    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- FontAwesome Icons -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css" rel="stylesheet">
    <!-- Google Fonts -->
    <link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;500;600;700;800&display=swap" rel="stylesheet">
    
    <style>
        body {
            font-family: 'Plus Jakarta Sans', sans-serif;
            background-color: #f1f3f6;
            color: #212121;
        }
        .navbar-flipkart {
            background-color: #2874f0;
            box-shadow: 0 2px 4px 0 rgba(0,0,0,.08);
        }
        .navbar-brand-title {
            font-weight: 800;
            font-style: italic;
            color: #ffffff !important;
            font-size: 1.5rem;
            letter-spacing: -0.5px;
        }
        .navbar-brand-sub {
            font-size: 0.75rem;
            color: #ffe500;
            font-weight: 600;
            margin-top: -6px;
        }
        .search-box input {
            border-radius: 4px 0 0 4px;
            border: none;
            box-shadow: none;
        }
        .search-box button {
            border-radius: 0 4px 4px 0;
            background-color: #ffffff;
            border: none;
            color: #2874f0;
        }
        .card-product {
            border: none;
            border-radius: 8px;
            transition: transform 0.2s ease, box-shadow 0.2s ease;
        }
        .card-product:hover {
            transform: translateY(-4px);
            box-shadow: 0 8px 16px rgba(0,0,0,0.1);
        }
        .price-tag {
            font-size: 1.25rem;
            font-weight: 700;
            color: #212121;
        }
        .btn-flipkart {
            background-color: #fb641b;
            color: #fff;
            font-weight: 700;
            border-radius: 4px;
            border: none;
        }
        .btn-flipkart:hover {
            background-color: #e65612;
            color: #fff;
        }
        .btn-cart {
            background-color: #ff9f00;
            color: #fff;
            font-weight: 700;
            border-radius: 4px;
            border: none;
        }
        .btn-cart:hover {
            background-color: #f09500;
            color: #fff;
        }
        .badge-category {
            background-color: #e0e0e0;
            color: #444;
            font-weight: 600;
            font-size: 0.75rem;
        }
        .star-rating {
            color: #388e3c;
            font-weight: 700;
            font-size: 0.85rem;
        }
    </style>
</head>
<body>

<!-- Navbar -->
<nav class="navbar navbar-expand-lg navbar-dark navbar-flipkart sticky-top py-2">
    <div class="container">
        <a class="navbar-brand d-flex flex-column me-4" href="${pageContext.request.contextPath}/home">
            <span class="navbar-brand-title"><i class="fa-solid fa-cart-shopping me-2"></i>LingeshMart</span>
            <span class="navbar-brand-sub">Explore <span style="color: #fff;">Plus</span></span>
        </a>

        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarMain">
            <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" id="navbarMain">
            <!-- Search Bar -->
            <form class="d-flex mx-auto search-box w-50" action="${pageContext.request.contextPath}/home" method="get">
                <input class="form-control px-3" type="search" name="q" value="<c:out value="${query}"/>" placeholder="Search for products, brands and more...">
                <button class="btn px-3" type="submit"><i class="fa-solid fa-magnifying-glass"></i></button>
            </form>

            <ul class="navbar-nav ms-auto align-items-center">
                <c:choose>
                    <c:when test="${not empty sessionScope.user}">
                        <c:if test="${sessionScope.user.role eq 'BUYER'}">
                            <li class="nav-item me-3">
                                <a class="nav-link text-white font-weight-bold" href="${pageContext.request.contextPath}/wishlist">
                                    <i class="fa-solid fa-heart me-1 text-warning"></i> Wishlist
                                </a>
                            </li>
                            <li class="nav-item me-3">
                                <a class="nav-link text-white font-weight-bold" href="${pageContext.request.contextPath}/cart">
                                    <i class="fa-solid fa-cart-shopping me-1"></i> Cart
                                </a>
                            </li>
                            <li class="nav-item me-3">
                                <a class="nav-link text-white font-weight-bold" href="${pageContext.request.contextPath}/orders">
                                    <i class="fa-solid fa-box-open me-1"></i> Orders
                                </a>
                            </li>
                        </c:if>

                        <c:if test="${sessionScope.user.role eq 'SELLER'}">
                            <li class="nav-item me-3">
                                <a class="btn btn-light text-primary font-weight-bold me-2" href="${pageContext.request.contextPath}/seller/dashboard">
                                    <i class="fa-solid fa-store me-1"></i> Seller Hub
                                </a>
                            </li>
                            <li class="nav-item me-3">
                                <a class="nav-link text-white" href="${pageContext.request.contextPath}/orders">
                                    <i class="fa-solid fa-clipboard-list me-1"></i> Incoming Orders
                                </a>
                            </li>
                        </c:if>

                        <c:if test="${sessionScope.user.role eq 'ADMIN'}">
                            <li class="nav-item me-3">
                                <a class="btn btn-warning text-dark font-weight-bold me-2" href="${pageContext.request.contextPath}/admin/dashboard">
                                    <i class="fa-solid fa-user-shield me-1"></i> Admin Panel
                                </a>
                            </li>
                        </c:if>

                        <li class="nav-item dropdown">
                            <a class="nav-link dropdown-toggle text-white fw-bold" href="#" data-bs-toggle="dropdown">
                                <i class="fa-solid fa-circle-user me-1 fs-5 align-middle"></i> <c:out value="${sessionScope.user.name}"/>
                            </a>
                            <ul class="dropdown-menu dropdown-menu-end shadow">
                                <li><span class="dropdown-item-text text-muted small">Role: <strong><c:out value="${sessionScope.user.role}"/></strong></span></li>
                                <li><hr class="dropdown-divider"></li>
                                <li><a class="dropdown-item text-danger" href="${pageContext.request.contextPath}/auth/logout"><i class="fa-solid fa-right-from-bracket me-2"></i> Logout</a></li>
                            </ul>
                        </li>
                    </c:when>

                    <c:otherwise>
                        <li class="nav-item">
                            <a class="btn btn-light text-primary px-4 fw-bold shadow-sm me-2" href="${pageContext.request.contextPath}/auth/login">Login</a>
                        </li>
                        <li class="nav-item">
                            <a class="btn btn-outline-light px-3 fw-bold" href="${pageContext.request.contextPath}/auth/register">Become a Seller / Sign Up</a>
                        </li>
                    </c:otherwise>
                </c:choose>
            </ul>
        </div>
    </div>
</nav>

<div class="container my-4">
    <!-- Display Alert Messages -->
    <c:if test="${not empty param.msg}">
        <div class="alert alert-success alert-dismissible fade show" role="alert">
            <i class="fa-solid fa-circle-check me-2"></i><c:out value="${param.msg}"/>
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>
    <c:if test="${not empty param.error}">
        <div class="alert alert-danger alert-dismissible fade show" role="alert">
            <i class="fa-solid fa-circle-exclamation me-2"></i><c:out value="${param.error}"/>
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>
