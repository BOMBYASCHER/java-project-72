@import hexlet.code.dto.urls.UrlPage

@param UrlPage page

@template.layout.page(
    page = page,
    content = @`
        <main>
            <div>
                <h1>Website: ${page.getUrl().getName()}</h1>
                <p>Created at: ${page.getUrl().getCreatedAtFormatted()}</p>
            </div>
        </main>
    `
)