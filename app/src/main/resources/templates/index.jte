@import hexlet.code.utils.NamedRoutes
@import hexlet.code.dto.MainPage

@param MainPage page

@template.layout.page(
    page = page,
    flash = page.getFlash(),
    content = @`
        <h1 class="text-body-emphasis">Checking the website for SEO suitability</h1>
        <p class="fs-5 col-md-8">Quickly and easily. Insert a website link to get started.</p>
        <div class="mb-5">
            <form action="${NamedRoutes.urlsPath()}" method="post" class="d-flex">
                <input class="form-control me-2" required type="search" placeholder="https://example.com" name="url" aria-label="Check">
                <button class="btn btn-outline-success" type="submit">Check</button>
            </form>
        </div>
    `
)
