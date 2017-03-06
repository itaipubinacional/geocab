import { GeocabPage } from './app.po';

describe('geocab App', function() {
  let page: GeocabPage;

  beforeEach(() => {
    page = new GeocabPage();
  });

  it('should display message saying app works', () => {
    page.navigateTo();
    expect(page.getParagraphText()).toEqual('app works!');
  });
});
