
AYNI Ecommerce 🛍️

AYNI es una plataforma de comercio electrónico desarrollada para una tienda online peruana. Su objetivo es ofrecer una experiencia de compra sencilla y segura, así como herramientas de administración para gestionar productos, inventario, pedidos y pagos desde un mismo sistema.

🌐 Sitio web: https://aynipe.com
Sobre el proyecto

AYNI reúne productos de distintas categorías, como hogar, belleza, cuidado personal, accesorios, tecnología y mascotas. La plataforma combina una tienda pública para clientes con un panel administrativo que permite organizar las operaciones del negocio.

El proyecto se desarrolla con una arquitectura frontend–backend–base de datos, separando la interfaz de usuario, la lógica de negocio y la persistencia de datos.
Tecnologías utilizadas
Capa	Tecnologías	Uso
Frontend	Angular, TypeScript, HTML, SCSS	Interfaz responsive, catálogo, formularios y administración
Backend	Java, Spring Boot, Spring Security	API REST, reglas de negocio, autenticación y autorización
Persistencia	Spring Data JPA, Hibernate, PostgreSQL	Gestión y almacenamiento de datos
Seguridad	JWT, Spring Security	Sesiones autenticadas y protección de rutas por roles
Infraestructura	Docker, Docker Compose, Nginx, VPS Contabo	Contenedores, proxy inverso y publicación en producción
Integraciones	Google Identity Services, servicio de LLM	Inicio de sesión con Google e iniciativas de asistencia inteligente; su disponibilidad depende del estado de integración
Funcionalidades principales
Tienda online

    Visualización de productos por categorías y páginas de detalle.
    Búsqueda y consulta de productos.
    Carrito de compras y proceso de checkout.
    Registro e inicio de sesión de usuarios.
    Gestión de pedidos y consulta de su estado.
    Registro de pagos mediante Yape, con envío de comprobantes para revisión administrativa.
    Secciones de productos destacados y ofertas.

Panel administrativo

    Creación, edición, listado y activación o desactivación de productos.
    Control de precios, SKU, stock y categorías.
    Carga de imágenes a almacenamiento persistente en el servidor.
    Datos ampliados de producto: descripción corta, imágenes de galería, beneficios y características configurables.
    Gestión y revisión de pedidos y comprobantes de pago.
    Funciones de administración de usuarios y visualización de información del negocio.

    Nota de alcance: algunas interfaces e integraciones, como la edición completa de galerías y atributos desde el administrador o el acceso con Google, han estado en evolución. Revisar el estado del código antes de describirlas como totalmente finalizadas.

Arquitectura general
                  CLIENTE / ADMIN
                         │
                  Angular + TypeScript
                         │ HTTPS
                         ▼
                       Nginx
                         │
                         ▼
                Spring Boot REST API
                  │             │
                  ▼             ▼
              PostgreSQL   Archivos subidos
                            (volumen persistente)

    Angular consume la API para presentar información y enviar operaciones.
    Spring Boot valida las solicitudes y concentra las reglas de negocio.
    PostgreSQL almacena usuarios, productos, pedidos y pagos.
    Docker Compose coordina los servicios y Nginx publica la aplicación por HTTPS.

Modelo de productos

La información de los productos se diseñó para adaptarse a varias categorías sin añadir columnas específicas para cada tipo de artículo.
Product
├── Datos básicos (nombre, slug, SKU, precio, stock, categoría)
├── Descripción y descripción corta
├── ProductImage[]    → galería y fotografías de detalle
├── ProductBenefit[]  → beneficios destacados
└── ProductFeature[]  → características flexibles

Este enfoque permite registrar, por ejemplo, conectividad en artículos tecnológicos, materiales en productos para el hogar o indicaciones de uso en artículos de belleza.
Flujo de compra

    El cliente explora el catálogo y consulta el detalle de un producto.
    Agrega artículos al carrito y realiza el checkout.
    El sistema registra el pedido.
    Si elige Yape, registra los datos de la operación y adjunta el comprobante.
    El administrador revisa el pago y gestiona el pedido.

Seguridad y buenas prácticas

    Autenticación con JWT y autorización basada en roles.
    Validaciones en solicitudes mediante el backend.
    Separación entre entidades, DTOs, repositorios, servicios y controladores.
    Configuración de credenciales y claves mediante variables de entorno.
    Gestión controlada de archivos e imágenes de productos y comprobantes.
    Enfoque en código mantenible, principios KISS y separación de responsabilidades.

    No publicar en el repositorio contraseñas, claves privadas, tokens JWT, archivos .env ni otros secretos.

Estructura general del repositorio
ayni-deploy/
├── frontend/                # Aplicación Angular
├── backend/
│   └── ayni-backend/        # API Spring Boot
├── docker-compose.yml      # Orquestación de servicios
└── README.md
Ejecución con Docker

Requisitos: Docker y Docker Compose, además de las variables de entorno requeridas por el proyecto.
# Desde la raíz del repositorio
docker compose config

docker compose up -d --build

docker compose ps

Los valores sensibles de la base de datos y las claves de servicios externos deben configurarse fuera del control de versiones. El despliegue de producción requiere certificados TLS y la configuración apropiada de Nginx y volúmenes persistentes.
API REST

Ejemplos de rutas del módulo de productos:
GET    /api/products
GET    /api/products/{slug}
GET    /api/products/featured
POST   /api/admin/products
PUT    /api/admin/products/{id}
POST   /api/admin/products/upload-image
POST   /api/admin/products/upload-detail-image

Las rutas administrativas requieren autenticación y permisos. La disponibilidad de otras rutas debe verificarse en los controladores de cada módulo.
Evolución del proyecto

Entre las líneas de mejora se encuentran:

    Completar las experiencias administrativas de galería, beneficios y características.
    Consolidar las integraciones de autenticación externa.
    Ampliar las capacidades de atención asistida por IA utilizando información real del catálogo.
    Mejorar la experiencia de compra, seguimiento de pedidos y reportes de ventas.

Propósito

AYNI es tanto una solución digital para un negocio real como un proyecto de ingeniería de software enfocado en desarrollo full stack, APIs REST, seguridad, modelado de datos, despliegue y escalabilidad.

AYNI Ecommerce · Tienda online peruana · https://aynipe.com
