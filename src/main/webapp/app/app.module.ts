import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import './vendor';
import { JhSweetHomeDinnerApplicationSharedModule } from 'app/shared/shared.module';
import { JhSweetHomeDinnerApplicationCoreModule } from 'app/core/core.module';
import { JhSweetHomeDinnerApplicationAppRoutingModule } from './app-routing.module';
import { JhSweetHomeDinnerApplicationHomeModule } from './home/home.module';
import { JhSweetHomeDinnerApplicationEntityModule } from './entities/entity.module';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import { MainComponent } from './layouts/main/main.component';
import { NavbarComponent } from './layouts/navbar/navbar.component';
import { FooterComponent } from './layouts/footer/footer.component';
import { PageRibbonComponent } from './layouts/profiles/page-ribbon.component';
import { ActiveMenuDirective } from './layouts/navbar/active-menu.directive';
import { ErrorComponent } from './layouts/error/error.component';

@NgModule({
  imports: [
    BrowserModule,
    JhSweetHomeDinnerApplicationSharedModule,
    JhSweetHomeDinnerApplicationCoreModule,
    JhSweetHomeDinnerApplicationHomeModule,
    // jhipster-needle-angular-add-module JHipster will add new module here
    JhSweetHomeDinnerApplicationEntityModule,
    JhSweetHomeDinnerApplicationAppRoutingModule,
  ],
  declarations: [MainComponent, NavbarComponent, ErrorComponent, PageRibbonComponent, ActiveMenuDirective, FooterComponent],
  bootstrap: [MainComponent],
})
export class JhSweetHomeDinnerApplicationAppModule {}
