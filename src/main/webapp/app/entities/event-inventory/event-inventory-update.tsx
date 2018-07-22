import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IProduct } from 'app/shared/model/product.model';
import { getEntities as getProducts } from 'app/entities/product/product.reducer';
import { IEvent } from 'app/shared/model/event.model';
import { getEntities as getEvents } from 'app/entities/event/event.reducer';
import { getEntity, updateEntity, createEntity, reset } from './event-inventory.reducer';
import { IEventInventory } from 'app/shared/model/event-inventory.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer } from 'app/shared/util/date-utils';
import { keysToValues } from 'app/shared/util/entity-utils';

export interface IEventInventoryUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: number }> {}

export interface IEventInventoryUpdateState {
  isNew: boolean;
  productId: number;
  eventId: number;
}

export class EventInventoryUpdate extends React.Component<IEventInventoryUpdateProps, IEventInventoryUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      productId: 0,
      eventId: 0,
      isNew: !this.props.match.params || !this.props.match.params.id
    };
  }

  componentDidMount() {
    if (this.state.isNew) {
      this.props.reset();
    } else {
      this.props.getEntity(this.props.match.params.id);
    }

    this.props.getProducts();
    this.props.getEvents();
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { eventInventoryEntity } = this.props;
      const entity = {
        ...eventInventoryEntity,
        ...values
      };

      if (this.state.isNew) {
        this.props.createEntity(entity);
      } else {
        this.props.updateEntity(entity);
      }
      this.handleClose();
    }
  };

  handleClose = () => {
    this.props.history.push('/entity/event-inventory');
  };

  productUpdate = element => {
    const id = element.target.value.toString();
    if (id === '') {
      this.setState({
        productId: -1
      });
    } else {
      for (const i in this.props.products) {
        if (id === this.props.products[i].id.toString()) {
          this.setState({
            productId: this.props.products[i].id
          });
        }
      }
    }
  };

  eventUpdate = element => {
    const id = element.target.value.toString();
    if (id === '') {
      this.setState({
        eventId: -1
      });
    } else {
      for (const i in this.props.events) {
        if (id === this.props.events[i].id.toString()) {
          this.setState({
            eventId: this.props.events[i].id
          });
        }
      }
    }
  };

  render() {
    const { eventInventoryEntity, products, events, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="misingpieceApp.eventInventory.home.createOrEditLabel">
              <Translate contentKey="misingpieceApp.eventInventory.home.createOrEditLabel">Create or edit a EventInventory</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : eventInventoryEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="event-inventory-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="itemCountLabel" for="itemCount">
                    <Translate contentKey="misingpieceApp.eventInventory.itemCount">Item Count</Translate>
                  </Label>
                  <AvField
                    id="event-inventory-itemCount"
                    type="number"
                    className="form-control"
                    name="itemCount"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                      number: { value: true, errorMessage: translate('entity.validation.number') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="creationDateLabel" for="creationDate">
                    <Translate contentKey="misingpieceApp.eventInventory.creationDate">Creation Date</Translate>
                  </Label>
                  <AvField id="event-inventory-creationDate" type="date" className="form-control" name="creationDate" />
                </AvGroup>
                <AvGroup>
                  <Label for="product.id">
                    <Translate contentKey="misingpieceApp.eventInventory.product">Product</Translate>
                  </Label>
                  <AvInput
                    id="event-inventory-product"
                    type="select"
                    className="form-control"
                    name="product.id"
                    onChange={this.productUpdate}
                  >
                    <option value="" key="0" />
                    {products
                      ? products.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="event.id">
                    <Translate contentKey="misingpieceApp.eventInventory.event">Event</Translate>
                  </Label>
                  <AvInput id="event-inventory-event" type="select" className="form-control" name="event.id" onChange={this.eventUpdate}>
                    <option value="" key="0" />
                    {events
                      ? events.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/event-inventory" replace color="info">
                  <FontAwesomeIcon icon="arrow-left" />&nbsp;
                  <span className="d-none d-md-inline">
                    <Translate contentKey="entity.action.back">Back</Translate>
                  </span>
                </Button>
                &nbsp;
                <Button color="primary" id="save-entity" type="submit" disabled={updating}>
                  <FontAwesomeIcon icon="save" />&nbsp;
                  <Translate contentKey="entity.action.save">Save</Translate>
                </Button>
              </AvForm>
            )}
          </Col>
        </Row>
      </div>
    );
  }
}

const mapStateToProps = (storeState: IRootState) => ({
  products: storeState.product.entities,
  events: storeState.event.entities,
  eventInventoryEntity: storeState.eventInventory.entity,
  loading: storeState.eventInventory.loading,
  updating: storeState.eventInventory.updating
});

const mapDispatchToProps = {
  getProducts,
  getEvents,
  getEntity,
  updateEntity,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(EventInventoryUpdate);
