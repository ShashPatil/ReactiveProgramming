package com.learnreactiveprogramming;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.Random;

public class FluxAndMonoGeneratorService {

    public Flux<String> namesFlux() {
        return Flux.fromIterable(List.of("alex", "ben", "chloe")).log();
    }

    public Mono<String> nameMono() {
        return Mono.just("alex");
    }



    public Flux<String> namesFlux_map(int stringLength) {
        //filter strings whose length is greater than 3
        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                .map(String::toUpperCase) //.map(s->s.toUpperCase())
                .filter(s -> s.length() > stringLength)
                .map(s -> s.length() + "-" + s);
        //.log();

    }


    //Reactive streams like Flux and Mono are immutable and lazy:
    public Flux<String> namesFlux_immutibility() {
        Flux<String> namedFlux = Flux.fromIterable(List.of("alex", "ben", "chloe"));
        namedFlux.map(String::toUpperCase);// This does nothing by itself!
        return namedFlux; // Returns original lowercase flux

    }
    public Mono<String> namesMono_map_filter(int stringLength) {
        return Mono.just("alex")
                .map(String::toUpperCase)
                .filter(s -> s.length() > stringLength);
    }
    //✅ Use flatMap here because splitStringMono(name) returns a Mono, not just a list.
    //flatmap transforms each element of a stream into zero or more elements of a new stream, and then flattens these resulting streams into a single, combined stream
    public Mono<List<String>> namesMono_flatMap(int stringLength) {
        return Mono.just("alex")
                .map(String::toUpperCase)
                .filter(s -> s.length() > stringLength)
                .flatMap(this::splitStringMono);
    }

    public Mono<List<String>> splitStringMono(String s) {
        String[] split = s.split("");
        List<String> split1 = List.of(split);
        return Mono.just(split1);
    }

    public Flux<String> namesFlux_flatmap(int stringLength) {
        //filter strings whose length is greater than 3
        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                .map(String::toUpperCase)
                .filter(s -> s.length() > stringLength)
                //ALEX,CFLOE -> A,L,E,X,C,H,L,O,E
                .flatMap(s -> splitString(s));


    }

    public Flux<String> namesFlux_flatmap_async(int stringLength) {
        //filter strings whose length is greater than 3
        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                .map(String::toUpperCase)
                .filter(s -> s.length() > stringLength)
                //ALEX,CFLOE -> A,L,E,X,C,H,L,O,E
                .flatMap(s -> splitString_withDelay(s));


    }

    //ALEX -> Flux(A,L,E,X)
    public Flux<String> splitString(String name) {
        String[] charArray = name.split("");
        return Flux.fromArray(charArray);
    }

    public Flux<String> splitString_withDelay(String name) {
        String[] charArray = name.split("");
        int delay = new Random().nextInt(1000);
        return Flux.fromArray(charArray)
                .delayElements(Duration.ofMillis(delay));
    }

    public static void main(String[] args) {

        FluxAndMonoGeneratorService fluxAndMonoGeneratorService = new FluxAndMonoGeneratorService();
        fluxAndMonoGeneratorService.namesFlux()
                .subscribe(name -> {
                    System.out.println("name is: " + name);
                });
        fluxAndMonoGeneratorService.nameMono()
                .subscribe(name -> System.out.println("mono name is: " + name));

        fluxAndMonoGeneratorService.namesFlux_map(3)
                .subscribe(name -> System.out.println("name using map is: " + name));


        fluxAndMonoGeneratorService.namesFlux_immutibility()
                .subscribe(name -> System.out.println("last one is: " + name));

        fluxAndMonoGeneratorService.namesFlux_flatmap(3)
                .subscribe(name -> System.out.println("using flatmap: " + name));

        fluxAndMonoGeneratorService.namesFlux_flatmap_async(3) //will not print anything because flatmap is used for asynchronous transformations
                .subscribe(name -> System.out.println("using flatmap: " + name));

        fluxAndMonoGeneratorService.namesMono_flatMap(3)
                .subscribe(name -> System.out.println("using mono_flatmap: " + name));
    }
}
