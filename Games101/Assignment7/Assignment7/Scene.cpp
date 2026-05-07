//
// Created by Göksu Güvendiren on 2019-05-14.
//

#include "Scene.hpp"


void Scene::buildBVH() {
    printf(" - Generating BVH...\n\n");
    this->bvh = new BVHAccel(objects, 1, BVHAccel::SplitMethod::NAIVE);
}

Intersection Scene::intersect(const Ray &ray) const
{
    return this->bvh->Intersect(ray);
}

void Scene::sampleLight(Intersection &pos, float &pdf) const
{
    float emit_area_sum = 0;
    for (uint32_t k = 0; k < objects.size(); ++k) {
        if (objects[k]->hasEmit()){
            emit_area_sum += objects[k]->getArea();
        }
    }
    float p = get_random_float() * emit_area_sum;
    emit_area_sum = 0;
    for (uint32_t k = 0; k < objects.size(); ++k) {
        if (objects[k]->hasEmit()){
            emit_area_sum += objects[k]->getArea();
            if (p <= emit_area_sum){
                objects[k]->Sample(pos, pdf);
                break;
            }
        }
    }
}

bool Scene::trace(
        const Ray &ray,
        const std::vector<Object*> &objects,
        float &tNear, uint32_t &index, Object **hitObject)
{
    *hitObject = nullptr;
    for (uint32_t k = 0; k < objects.size(); ++k) {
        float tNearK = kInfinity;
        uint32_t indexK;
        Vector2f uvK;
        if (objects[k]->intersect(ray, tNearK, indexK) && tNearK < tNear) {
            *hitObject = objects[k];
            tNear = tNearK;
            index = indexK;
        }
    }


    return (*hitObject != nullptr);
}

// Implementation of Path Tracing
Vector3f Scene::castRay(const Ray &ray, int depth) const
{
    auto block_inter = intersect(ray);
    if(!block_inter.happened) return Vector3f(0);
    Vector3f &p = block_inter.coords;
    Vector3f &n = block_inter.normal;
    Material* m = block_inter.m;
    Vector3f wo = ray.direction; 
    if(m->hasEmission()) return m->getEmission();
    Vector3f L_dir(0),
             L_indir(0);

    Intersection light_inter;
    float pdf_light;
    sampleLight(light_inter,pdf_light);
    Vector3f x = light_inter.coords;
    Vector3f nn = light_inter.normal;
    Vector3f emit = light_inter.emit;
    Vector3f ws = (x-p).normalized();
    //Shoot a ray from p to x
    Intersection light_collider_inter = intersect(Ray(p,ws));
    float dis_x2p = (x-p).norm();
    //Detect whether there is something else on the way
    if(abs(light_collider_inter.distance-dis_x2p) < 0.01f){
        //If there is, calculate L_dir
        L_dir = emit * m->eval(wo,ws,n) * dotProduct(ws,n) * dotProduct(-ws,nn) / (dis_x2p*dis_x2p*pdf_light);
    }
    
    //Use RussianRoulette to decide casting a ray or not
    if(get_random_float() < RussianRoulette){
        auto wi = (m->sample(wo,n)).normalized();
        auto pdf = m->pdf(wo,wi,n);
        auto ray_wi = Ray(p,wi);
        auto wi_inter = intersect(ray_wi);
        
        if(wi_inter.happened && !(wi_inter.m->hasEmission())){
            //if pdf is not too small, calculate L_indir
            if(pdf > EPSILON){
                L_indir = castRay(ray_wi,depth+1) * m->eval(wo,wi,n) * dotProduct(wi,n) /(RussianRoulette * pdf);
            }
        }
    }
    return L_dir + L_indir;
}

Vector3f Scene::shade(const Intersection &inter, const Vector3f &wo) const{
    if(!inter.happened) return Vector3f(0);
    if(inter.m->hasEmission()) return  inter.m->getEmission();
    Vector3f p = inter.coords, n = inter.normal;
    Material* m = inter.m;
    Vector3f L_dir(0),L_indir(0);

    Intersection interChecker; 
    float pdf_light;
    sampleLight(interChecker,pdf_light);

    Vector3f x = interChecker.coords;
    Vector3f nn = interChecker.normal; 
    Vector3f emit = interChecker.emit; 
    Vector3f ws = normalize(x-p); // ws : p -> x
    float disxp = (x-p).norm();
    interChecker = intersect(Ray(p,ws));
    if(interChecker.distance - disxp < 0.01f)
    {
        L_dir = emit * m->eval(wo,ws,n) * dotProduct(ws,n) *dotProduct(-ws,nn) / (disxp * disxp * pdf_light);
    }

    if(get_random_float() < RussianRoulette){
        Vector3f wi = normalize(m->sample(wo,n));
        interChecker = intersect(Ray(p,wi));
        if(interChecker.happened && !interChecker.m->hasEmission()){
            auto pdf = m->pdf(wi,wo,n);
            if(pdf > EPSILON){
                L_indir =  shade(interChecker,wi) * m->eval(wi,wo,n) * dotProduct(wi,n) / ( pdf * RussianRoulette);
            }
        }
    }
    return L_indir + L_dir;
}