package growinupscala.controllers

import growinupscala.{Id, JSON}

trait Controller[R] {

  def get(id: Id): R

  def post(resource: JSON): R

  def put(id: Id, resource: JSON): R

  def delete(id: Id): R

}
