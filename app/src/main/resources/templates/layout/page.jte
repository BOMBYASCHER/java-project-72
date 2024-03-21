@import gg.jte.Content
@import hexlet.code.dto.flash.Flash
@import hexlet.code.utils.NamedRoutes
@import hexlet.code.dto.BasePage

@param BasePage page = null
@param Content content
@param Flash flash = null

<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous"><link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <title>Page analyzer</title>
</head>
<body>
    @if(flash != null)
    <div class="alert alert-${flash.getType().name().toLowerCase()}" role="alert">
        ${page.getFlash().getText()}
    </div>
    @endif
    <div class="col-lg-8 mx-auto p-4 py-md-5">
        <header class="d-flex align-items-center pb-3 mb-5 border-bottom">
            <nav class="nav">
                <a href="${NamedRoutes.rootPath()}" class="nav-link d-flex align-items-center text-body-emphasis text-decoration-none">
                    <span class="fs-4">Page analyzer</span>
                </a>
                <a href="${NamedRoutes.urlsPath()}" class="nav-link d-flex align-items-center text-body-emphasis text-decoration-none">
                    <span class="fs-4">Websites</span>
                </a>
            </nav>
        </header>
        <main>
            ${content}
        </main>
        <footer class="pt-5 my-5 text-body-secondary border-top">
            <p>Created by <a href="https://github.com/BOMBYASCHER">Me</a> · © 2024</p>
        </footer>
    </div>
</body>
</html>
