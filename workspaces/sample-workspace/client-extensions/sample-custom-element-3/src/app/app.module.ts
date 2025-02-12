import { Injector, NgModule } from '@angular/core';
import { createCustomElement } from "@angular/elements";
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';

@NgModule({
  entryComponents: [AppComponent],
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule
  ],
  providers: [],
})
export class AppModule {
 
  constructor(private injector: Injector) {}
 
  ngDoBootstrap() {
    const AppComponentElement = 
      createCustomElement(AppComponent, {
        injector: this.injector
      });
 
    customElements.define('sample-custom-element-3',
      AppComponentElement
    );
  }
 
}
