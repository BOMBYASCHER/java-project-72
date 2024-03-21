@import hexlet.code.dto.urls.UrlsPage
@import hexlet.code.utils.NamedRoutes
@param UrlsPage page

@template.layout.page(
    content = @`
        <h1 class="text-body-emphasis">Websites</h1>
        <p class="fs-5 col-md-8">Checking the websites may take some time.</p>
        <div class="mb-5">
            @if(page.getUrls().isEmpty())
            <p>No added websites yet.</p>
            @endif
            <table class="table table-striped">
                <thead>
                <tr>
                    <th scope="col">ID</th>
                    <th scope="col">URL</th>
                    <th scope="col">Added at</th>
                </tr>
                </thead>
                <tbody>
                @if(!page.getUrls().isEmpty())
                @for(var url : page.getUrls())
                    <tr>
                        <th scope="row">${url.getId()}</th>
                        <td><a href="${NamedRoutes.urlPath(url.getId())}">${url.getName()}</a></td>
                        <td>${url.getCreatedAtFormatted()}</td>
                    </tr>
                @endfor
                @endif
                </tbody>
            </table>
        </div>
    `
)